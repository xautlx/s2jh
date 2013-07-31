package lab.s2jh.biz.xs.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import lab.s2jh.biz.core.web.BaseBizController;
import lab.s2jh.biz.xs.entity.XsJbxx;
import lab.s2jh.biz.xs.entity.XsXxImport;
import lab.s2jh.biz.xs.entity.XsXxImport.XlsMatchTitles;
import lab.s2jh.biz.xs.service.XsJbxxService;
import lab.s2jh.biz.xs.service.XsXxImportService;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.biz.xx.service.XxBjService;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.security.AclService;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.struts2.rest.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@MetaData(title = "学籍导入")
public class XsXxImportController extends BaseBizController<XsXxImport, String> {

    private final Logger logger = LoggerFactory.getLogger(XsXxImportController.class);

    @Autowired
    private XsXxImportService xsXxImportService;

    @Autowired
    private XsJbxxService xsJbxxService;

    @Autowired
    private XxBjService xxBjService;

    @Autowired
    private AclService aclService;

    @Override
    protected BaseService<XsXxImport, String> getEntityService() {
        return xsXxImportService;
    }

    @Override
    protected void checkEntityAclPermission(XsXxImport entity) {
        aclService.validateAuthUserAclCodePermission(entity.getXxdm());
    }

    @Override
    @MetaData(title = "查询")
    public HttpHeaders findByPage() {
        return super.findByPage();
    }

    //处理附件文件上传
    private File[] uploadFiles;

    private String[] uploadFilesFileName;

    private String[] uploadFilesContentType;

    public void setUploadFiles(File[] uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public void setUploadFilesFileName(String[] uploadFilesFileName) {
        this.uploadFilesFileName = uploadFilesFileName;
    }

    public void setUploadFilesContentType(String[] uploadFilesContentType) {
        this.uploadFilesContentType = uploadFilesContentType;
    }

    @MetaData(title = "导入数据文件上传")
    public HttpHeaders doUploadFile() throws Exception {
        Date now = new Date();
        String sessionId = this.getRequest().getSession().getId();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (int i = 0; i < uploadFiles.length; i++) {
            logger.debug("Upload file: {} : {}", uploadFilesFileName[i], uploadFilesContentType[i]);

            List<XsXxImport> xsXxImports = Lists.newArrayList();

            InputStream is = new BufferedInputStream(new FileInputStream(uploadFiles[i]));
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            // 循环每一个sheet
            for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
                // 获得一个sheet
                HSSFSheet aSheet = workbook.getSheetAt(numSheets);
                // 判断sheet是否为空
                if (null == aSheet) {
                    continue;
                }
                // 获取sheet的第一行数
                // System.out.println("+++getFirstRowNum+++"
                // +aSheet.getFirstRowNum());
                // 获取sheet的最后一行数
                int lastRowNum = aSheet.getLastRowNum();

                Field[] fields = XsXxImport.class.getDeclaredFields();
                Map<Integer, String> idxPropertyMapping = Maps.newHashMap();
                Map<Integer, String> idxTitleMapping = Maps.newHashMap();
                //获取第一行标题行
                Set<String> titles = Sets.newLinkedHashSet();
                HSSFRow firstRow = aSheet.getRow(0);

                int lastCellNum = firstRow.getLastCellNum();
                logger.debug("Sheet: {}, last column: {}, last row number: {}", aSheet.getSheetName(),
                        CellReference.convertNumToColString(lastCellNum), lastRowNum);

                for (int cellNumOfRow = 0; cellNumOfRow < lastCellNum; cellNumOfRow++) {
                    HSSFCell aCell = firstRow.getCell(cellNumOfRow);
                    String columnLetter = CellReference.convertNumToColString(cellNumOfRow);
                    Assert.notNull(aCell, "标题栏位不能为空.错误列编号:" + columnLetter);
                    String title = aCell.getStringCellValue().trim();
                    Assert.isTrue(!titles.contains(title), "出现重复的标题名:" + title + ",错误列编号:" + columnLetter);

                    //检查标题是否有对应接收对象属性定义
                    boolean match = false;
                    for (Field field : fields) {
                        XlsMatchTitles xlsMatchTitles = field.getAnnotation(XlsMatchTitles.class);
                        if (xlsMatchTitles != null) {
                            for (String one : xlsMatchTitles.value()) {
                                if (one.equalsIgnoreCase(title)) {
                                    idxPropertyMapping.put(cellNumOfRow, field.getName());
                                    idxTitleMapping.put(cellNumOfRow, title);
                                    match = true;
                                    break;
                                }
                            }
                        }
                    }
                    Assert.isTrue(match, "无效的标题名:" + title + ",错误列编号:" + columnLetter);
                    titles.add(title);
                }

                // 循环每一行
                for (int rowNumOfSheet = 1; rowNumOfSheet <= lastRowNum; rowNumOfSheet++) {
                    // 判断是否为空行
                    if (null != aSheet.getRow(rowNumOfSheet)) {
                        // 获取一个行引用
                        HSSFRow aRow = aSheet.getRow(rowNumOfSheet);
                        // 循环每一列
                        XsXxImport xsXxImport = new XsXxImport();
                        xsXxImport.setGroupNum(sessionId);
                        xsXxImport.setLineNum(rowNumOfSheet);
                        xsXxImport.setCreatedDate(now);

                        for (int cellNumOfRow = 0; cellNumOfRow <= aRow.getLastCellNum(); cellNumOfRow++) {
                            // 判断列是否为空
                            if (null != aRow.getCell(cellNumOfRow)) {
                                // 获取列引用
                                HSSFCell aCell = aRow.getCell(cellNumOfRow);
                                String columnLetter = CellReference.convertNumToColString(cellNumOfRow);
                                String strCell = aCell.getStringCellValue();
                                String propertyName = idxPropertyMapping.get(cellNumOfRow);
                                FieldUtils.writeDeclaredField(xsXxImport, propertyName, strCell, true);

                                //属性校验
                                Set<ConstraintViolation<XsXxImport>> constraintViolations = validator.validateProperty(
                                        xsXxImport, propertyName);
                                if (!CollectionUtils.isEmpty(constraintViolations)) {
                                    for (ConstraintViolation<XsXxImport> constraintViolation : constraintViolations) {
                                        xsXxImport.addValidateMessage("行号=" + (rowNumOfSheet + 1) + ",列名=" + "("
                                                + columnLetter + ")" + idxTitleMapping.get(cellNumOfRow) + " : "
                                                + constraintViolation.getMessage());
                                    }
                                }
                            }
                        }
                        logger.debug("Add XsJbxx Import Record: {}", xsXxImport);
                        xsXxImports.add(xsXxImport);
                    }
                }
            }

            xsXxImportService.uploadImportData(sessionId, xsXxImports);
        }
        setModel(OperationResult.buildSuccessResult("导入数据文件上传操作完成"));
        return buildDefaultHttpHeaders();
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupFilter) {
        super.appendFilterProperty(groupFilter);
        groupFilter.and(new PropertyFilter(MatchType.EQ, "groupNum", this.getRequest().getSession().getId()));
    }

    @MetaData(title = "执行导入有效数据")
    public HttpHeaders doImport() {
        String groupNum = this.getRequest().getSession().getId();
        Field[] fields = XsXxImport.class.getDeclaredFields();
        List<XsXxImport> xsXxImports = xsXxImportService.findByGroupNumAndValidatePass(groupNum);
        for (XsXxImport xsXxImport : xsXxImports) {
            try {
                XsJbxx xsJbxx = new XsJbxx();
                //xsJbxx.setXxdm(AuthContextHolder.getAclCode());
                for (Field field : fields) {
                    String fieldName = field.getName();
                    XlsMatchTitles xlsMatchTitles = field.getAnnotation(XlsMatchTitles.class);
                    if (xlsMatchTitles != null) {
                        Object src = FieldUtils.readDeclaredField(xsXxImport, fieldName, true);
                        if (fieldName.equals("ssbj")) {
                            XxBj xxBj = xxBjService.findOne(String.valueOf(src));
                            xsJbxx.setXxBj(xxBj);
                            xsJbxx.setXxdm(xxBj.getXxdm());
                            continue;
                        }

                        FieldUtils.writeDeclaredField(xsJbxx, fieldName, src, true);
                    }
                }
                xsJbxxService.save(xsJbxx);
            } catch (Exception e) {
                xsXxImport.addImportMessage(e.getMessage());
                xsXxImportService.save(xsXxImport);
            }
        }
        setModel(OperationResult.buildSuccessResult("导入数据文件上传操作完成"));
        return buildDefaultHttpHeaders();
    }
}