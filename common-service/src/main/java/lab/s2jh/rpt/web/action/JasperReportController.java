package lab.s2jh.rpt.web.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
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
import lab.s2jh.sys.service.DataDictService;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.HttpHeaders;
import org.apache.struts2.views.jasperreports.JasperReportConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.FileCopyUtils;

import com.google.common.collect.Maps;

public class JasperReportController extends BaseController<ReportDef, String> {

    private final Logger logger = LoggerFactory.getLogger(JasperReportController.class);

    private static String WEB_ROOT_DIR = null;

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

    private String getWebRootDir() {
        if (WEB_ROOT_DIR == null) {
            WEB_ROOT_DIR = ServletActionContext.getServletContext().getRealPath("/");
        }
        return WEB_ROOT_DIR;
    }

    private String getRelativeJasperFilePath() {
        return File.separator + "WEB-INF" + File.separator + JASPER_TEMPLATE_FILE_DIR;
    }

    private File getTargetJasperFile(String reportId) {
        return new File(getWebRootDir() + getRelativeJasperFilePath() + File.separator + reportId + ".jasper");
    }

    @SecurityControllIgnore
    public String getLocation() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String reportId = request.getParameter("report");
        File targetJasperFile = getTargetJasperFile(reportId);
        File targetJrxmlFile = new File(getWebRootDir() + getRelativeJasperFilePath() + File.separator + reportId
                + ".jrxml");
        try {
            if (!targetJrxmlFile.exists()) {
                targetJrxmlFile.createNewFile();
            }
            logger.debug("Using jrxml file: {}", targetJrxmlFile.getAbsolutePath());
            logger.debug("Using jasper file: {}", targetJasperFile.getAbsolutePath());
            ReportDef reportDef = reportDefService.findByCode(reportId);
            AttachmentFile attachmentFile = null;
            if (reportDef != null) {
                attachmentFile = reportDef.getTemplateFile();
            }
            boolean needUpdateJasperFile = false;
            if (!targetJasperFile.exists()) {
                needUpdateJasperFile = true;
                if (!targetJasperFile.exists()) {
                    targetJasperFile.createNewFile();
                }
            } else {

                if (attachmentFile != null) {
                    //数据对象判断处理
                    long compareTime = attachmentFile.getLastModifiedDate() != null ? attachmentFile
                            .getLastModifiedDate().getTime() : attachmentFile.getCreatedDate().getTime();
                    if (targetJasperFile.lastModified() < compareTime) {
                        needUpdateJasperFile = true;
                        FileCopyUtils.copy(attachmentFile.getFileContent(), targetJrxmlFile);
                    }
                } else {
                    if (targetJrxmlFile.lastModified() > targetJasperFile.lastModified()) {
                        needUpdateJasperFile = true;
                    }
                }
            }
            if (needUpdateJasperFile) {
                logger.info("Compiling jasper file: {}", targetJasperFile.getAbsolutePath());
                JasperCompileManager.compileReportToFile(targetJrxmlFile.getAbsolutePath(),
                        targetJasperFile.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebException(e.getMessage(), e);
        }
        return getRelativeJasperFilePath() + File.separator + reportId + ".jasper";
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
    private Map<String, Object> reportParameters = Maps.newHashMap();

    public Map<String, Object> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(Map<String, Object> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public Map<String, Object> getJasperReportParameters() {
        Map<String, Object> jasperReportParameters = Maps.newHashMap();
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            String reportId = request.getParameter("report");
            File targetJasperFile = getTargetJasperFile(reportId);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(targetJasperFile);
            JRParameter[] params = jasperReport.getParameters();

            for (Map.Entry<String, Object> val : reportParameters.entrySet()) {
                String key = val.getKey();

                if (val.getValue() == null) {
                    continue;
                }
                String[] vals = (String[]) val.getValue();
                for (JRParameter param : params) {
                    if (!param.isSystemDefined() && param.isForPrompting()) {
                        String name = param.getName();
                        Class<?> clazz = param.getValueClass();
                        if (!name.equals(key)) {
                            continue;
                        }
                        //TODO: 先初步添加集合处理，后续逐步添加数字、日期等类型转换处理
                        if (Collection.class.isAssignableFrom(clazz)) {
                            //集合类型参数处理，TODO: 可以考虑进一步添加param.getNestedType()处理
                            jasperReportParameters.put(key, vals);
                        } else {
                            //其余情况把参数转换为普通字符串传入
                            jasperReportParameters.put(val.getKey(), StringUtils.join(","));
                        }
                    }
                }
            }
            jasperReportParameters.put("_RPT_ID", reportId);
            jasperReportParameters.put("_RPT_FORMAT", this.getFormat());
            String url = request.getRequestURL().toString();
            logger.debug("Report URL: " + url);
            jasperReportParameters.put("_RPT_URL", url);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WebException(e.getMessage(), e);
        }
        return jasperReportParameters;
    }

    public void prepareShow() {
        String report = this.getRequiredParameter("report");
        ReportDef reportDef = reportDefService.findByCode(report);
        setModel(reportDef);
    }

    @MetaData(value = "报表显示")
    public HttpHeaders show() {
        return buildDefaultHttpHeaders("show");
    }

    /**
     * 查询数据字典类别对应数据集合Map
     * @param primaryKey 数据字典类别代码
     * @return
     */
    public Map<String, String> getDataDictKeyValueMap(String primaryKey) {
        return dataDictService.findMapDataByPrimaryKey(primaryKey);
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
                        value = entityComment.value();
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
