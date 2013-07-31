package lab.s2jh.rpt.web.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.annotation.SecurityControllIgnore;
import lab.s2jh.rpt.entity.ReportDef;
import lab.s2jh.rpt.service.ReportDefService;
import lab.s2jh.sys.entity.AttachmentFile;
import lab.s2jh.sys.entity.DataDict;
import lab.s2jh.sys.service.DataDictService;
import net.sf.jasperreports.engine.JasperCompileManager;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.views.jasperreports.JasperReportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.FileCopyUtils;

import com.google.common.collect.Maps;

public class JasperReportController extends BaseController<ReportDef, String> {

    private final Logger logger = LoggerFactory.getLogger(JasperReportController.class);

    private static final String JASPER_TEMPLATE_FILE_DIR = File.separator + "template" + File.separator + "jasper";

    @Autowired
    private ReportDefService reportDefService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataDictService dataDictService;

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new WebException(e.getMessage(), e);
        }
    }

    @Override
    protected BaseService<ReportDef, String> getEntityService() {
        return reportDefService;
    }

    @Override
    protected void checkEntityAclPermission(ReportDef entity) {
        // Do nothing
    }

    @SecurityControllIgnore
    public String generate() {
        return "jasperResult";
    }

    @SecurityControllIgnore
    public String getLocation() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String reportId = request.getParameter("report");
        String rootPath = ServletActionContext.getServletContext().getRealPath("/");
        String targetJasperFilePath = JASPER_TEMPLATE_FILE_DIR + File.separator + reportId + ".jasper";
        try {
            File targetJasperFile = new File(rootPath + targetJasperFilePath);
            ReportDef reportDef = reportDefService.findByCode(reportId);
            AttachmentFile attachmentFile = reportDef.getTemplateFile();
            boolean needUpdateJasperFile = false;
            if (!targetJasperFile.exists()) {
                needUpdateJasperFile = true;
                new File(rootPath + JASPER_TEMPLATE_FILE_DIR).mkdirs();
                if (!targetJasperFile.exists()) {
                    targetJasperFile.createNewFile();
                }
            } else {
                if (attachmentFile != null) {
                    long compareTime = attachmentFile.getLastModifiedDate() != null ? attachmentFile
                            .getLastModifiedDate().getTime() : attachmentFile.getCreatedDate().getTime();
                    if (targetJasperFile.lastModified() < compareTime) {
                        needUpdateJasperFile = true;
                    }
                }
            }
            if (needUpdateJasperFile) {
                File targetJrxmlFile = new File(rootPath + JASPER_TEMPLATE_FILE_DIR + File.separator + reportId
                        + ".jrxml");
                if (!targetJrxmlFile.exists()) {
                    targetJrxmlFile.createNewFile();
                }
                if (attachmentFile != null) {
                    FileCopyUtils.copy(attachmentFile.getFileContent(), targetJrxmlFile);
                } else {
                    String sourceJrxmlFile = JASPER_TEMPLATE_FILE_DIR + File.separator + reportId + ".jrxml";
                    Resource resource = new ClassPathResource(sourceJrxmlFile);
                    FileCopyUtils.copy(FileCopyUtils.copyToByteArray(resource.getInputStream()), targetJrxmlFile);
                }
                JasperCompileManager.compileReportToFile(targetJrxmlFile.getAbsolutePath(),
                        targetJasperFile.getAbsolutePath());
                logger.info("Jasper file path: {}", targetJasperFile.getAbsolutePath());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebException(e.getMessage(), e);
        }
        return targetJasperFilePath;
    }

    private static Map<String, String> jasperOutputFormatMap;

    public Map<String, String> getJasperOutputFormatMap() {
        if (jasperOutputFormatMap == null) {
            jasperOutputFormatMap = new LinkedHashMap<String, String>();
            jasperOutputFormatMap.put(JasperReportConstants.FORMAT_PDF, "Adobe PDF");
            jasperOutputFormatMap.put(JasperReportConstants.FORMAT_XLS, "Excel");
            jasperOutputFormatMap.put(JasperReportConstants.FORMAT_HTML, "HTML");
        }
        return jasperOutputFormatMap;
    }

    @SecurityControllIgnore
    public String getFormat() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String format = request.getParameter("format");
        if (StringUtils.isBlank(format) || !getJasperOutputFormatMap().containsKey(format)) {
            format = JasperReportConstants.FORMAT_PDF;
        }
        return format;
    }

    @SecurityControllIgnore
    public String getContentDisposition() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String contentDisposition = request.getParameter("contentDisposition");
        if (StringUtils.isBlank(contentDisposition)) {
            contentDisposition = "inline";
        }
        return contentDisposition;
    }

    @SecurityControllIgnore
    public String getDocumentName() {
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            String reportName = request.getParameter("report");
            return new String(reportName.getBytes("GBK"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.warn("Chinese file name encoding error", e);
            return "export-data";
        }
    }

    /** JasperReport输入参数Map */
    private Map<String, String> reportParameters = Maps.newHashMap();

    public Map<String, String> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(Map<String, String> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public Map<String, Object> getJasperReportParameters() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Map<String, Object> jasperReportParameters = Maps.newHashMap();
        for (Map.Entry<String, String> val : reportParameters.entrySet()) {
            if (val.getValue() == null) {
                continue;
            }
            if (val.getValue() instanceof String && StringUtils.isBlank(String.valueOf(val.getValue()))) {
                continue;
            }
            jasperReportParameters.put(val.getKey(), val.getValue());
        }
        String reportId = request.getParameter("report");
        jasperReportParameters.put("_RPT_ID", reportId);
        jasperReportParameters.put("_RPT_FORMAT", this.getFormat());
        String url = request.getRequestURL().toString();
        logger.debug("Report URL: " + url);
        jasperReportParameters.put("_RPT_URL", url);
        return jasperReportParameters;
    }

    public void prepareShow() {
        String report = this.getRequiredParameter("report");
        ReportDef reportDef = reportDefService.findByCode(report);
        setModel(reportDef);
    }

    @MetaData(title = "报表显示")
    public HttpHeaders show() {
        return buildDefaultHttpHeaders("show");
    }

    /**
     * 查询数据字典类别对应数据集合Map
     * @param category 数据字典类别代码
     * @return
     */
    public Map<String, String> getDataDictKeyValueMap(String category) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            List<DataDict> dataDicts = dataDictService.findByCategory(category);
            for (DataDict dataDict : dataDicts) {
                dataMap.put(dataDict.getKey1Value(), dataDict.getData1Value());
            }
        } catch (Exception e) {
            logger.error("DataDict parse error: " + category, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
     * 根据SQL查询对应数据集合Map
     * @param sql 返回Key-Value形式的SQL语句
     * @return
     */
    public Map<String, String> getSQLKeyValueMap(String sql) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(sql)) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
                while (row.next()) {
                    dataMap.put(row.getString(1), row.getString(2));
                }
            }
        } catch (Exception e) {
            logger.error("SQL parse error: " + sql, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
     * 根据Enum Class字符串获取对应数据集合Map
     * @param enumClass Enum Class对应的完整类路径
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map<String, String> getEnumKeyValueMap(String enumClass) {
        Map<String, String> dataMap = new LinkedHashMap<String, String>();
        try {
            if (StringUtils.isNotBlank(enumClass)) {
                Class clazz = Class.forName(enumClass);
                for (Field enumfield : clazz.getFields()) {
                    MetaData entityComment = enumfield.getAnnotation(MetaData.class);
                    String value = enumfield.getName();
                    if (entityComment != null) {
                        value = entityComment.title();
                    }
                    dataMap.put(enumfield.getName(), value);
                }
            }
        } catch (Exception e) {
            //由于此异常出现在JSP页面解析过程，无法转向到全局的errors错误显示页面，因此采用logger记录error信息
            logger.error("Enum parse error: " + enumClass, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }

    /**
     * 将OGNL字符串转化为对应数据集合Map
     * @param ognl OGNL语法字符串，如：#{'A':'ClassA','B':'ClassB'}
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map getOGNLKeyValueMap(String ognl) throws OgnlException {
        Map dataMap = new LinkedHashMap();
        try {
            if (StringUtils.isNotBlank(ognl)) {
                dataMap = (Map) Ognl.getValue(ognl, null);
            }
        } catch (Exception e) {
            //由于此异常出现在JSP页面解析过程，无法转向到全局的errors错误显示页面，因此采用logger记录error信息
            logger.error("Ognl parse error: " + ognl, e);
            dataMap.put("ERROR", "[系统处理出现异常]");
        }
        return dataMap;
    }
}
