package lab.s2jh.biz.xs.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.xs.imp.validation.EnumTypeValue;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.validation.FormattedDate;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "BIZ_XS_XXIMPORT")
@MetaData(value = "学生信息导入中间处理表")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XsXxImport extends PersistableEntity<String> {

    @NotNull
    @XlsMatchTitles({ "xxdm", "学校代码", "所属学校" })
    private String xxdm;

    @NotNull
    @XlsMatchTitles({ "ssbj", "所属班级" })
    private String ssbj;

    @NotNull
    @Size(min = 1, max = 20)
    @XlsMatchTitles({ "xh", "学号" })
    private String xh;

    @NotNull
    @Size(min = 2, max = 36)
    @XlsMatchTitles({ "xm", "姓名" })
    private String xm;

    @NotNull
    @Size(min = 1, max = 1)
    @EnumTypeValue(EnumTypes.ZD_GB_XBM)
    @XlsMatchTitles({ "xbm", "性别", "性别码" })
    private String xbm;

    @NotNull
    @Size(min = 8, max = 8)
    @FormattedDate(pattern = "yyyyMMdd")
    @XlsMatchTitles({ "csrq", "出生日期" })
    private String csrq;

    @NotNull
    @Size(min = 2, max = 36)
    @XlsMatchTitles({ "csdm", "出生地", "出生地码" })
    private String csdm;

    @XlsMatchTitles({ "jg", "籍贯" })
    private String jg;

    @EnumTypeValue(EnumTypes.ZD_GB_MZM)
    @XlsMatchTitles({ "mzm", "民族", "民族码" })
    private String mzm;

    @NotNull
    @EnumTypeValue(EnumTypes.ZD_GB_GJDQM)
    @XlsMatchTitles({ "gjdqm", "国籍/地区", "国籍/地区码码" })
    private String gjdqm;

    @EnumTypeValue(EnumTypes.ZD_BB_SFZJLXM)
    @XlsMatchTitles({ "sfzjlxm", "身份证件类型" })
    private String sfzjlxm;

    @Size(min = 15, max = 18)
    @XlsMatchTitles({ "sfzjh", "身份证件号" })
    private String sfzjh;

    @EnumTypeValue(EnumTypes.ZD_BB_GATQWM)
    @XlsMatchTitles({ "gatqwm", "港澳台侨外码" })
    private String gatqwm;

    @NotNull
    @EnumTypeValue(EnumTypes.ZD_BB_ZZMMM)
    @XlsMatchTitles({ "zzmmm", "政治面貌", "政治面貌码" })
    private String zzmmm;

    @NotNull
    @EnumTypeValue(EnumTypes.ZD_GB_JKZKM)
    @XlsMatchTitles({ "jkzkm", "健康状况", "健康状况码" })
    private String jkzkm;

    @XlsMatchTitles({ "grbsm", "个人标识码" })
    private String grbsm;

    @MetaData(value = "分组标识", description = "一般取用户Session标识一批次操作分组")
    @EntityAutoCode(order = 10, search = false, listShow = false)
    private String groupNum;

    @MetaData(value = "文件名称", description = "记录导入文件名称")
    @EntityAutoCode(order = 10, search = false, listHidden = true)
    private String fileName;

    @MetaData(value = "Excel Sheet名称", description = "记录导入Sheet名称")
    @EntityAutoCode(order = 20, search = false, listHidden = true)
    private String sheetName;

    @MetaData(value = "Excel行号")
    @EntityAutoCode(order = 30, search = true)
    private Integer lineNum;

    @MetaData(value = "校验通过")
    @EntityAutoCode(order = 1000, search = true)
    private Boolean validatePass = Boolean.TRUE;

    @MetaData(value = "校验未过说明")
    @EntityAutoCode(order = 1100)
    private String validateMessage;

    @MetaData(value = "导入通过")
    @EntityAutoCode(order = 1200, search = true)
    private Boolean importPass = Boolean.TRUE;

    @MetaData(value = "导入未过说明")
    @EntityAutoCode(order = 1300)
    private String importMessage;

    private String id;

    private Date createdDate;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    @Transient
    public String getDisplay() {
        return xm;
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return createdDate;
    }

    @SkipParamBind
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(nullable = true, length = 1000)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(nullable = true, length = 1000)
    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    @Column(nullable = false, length = 256)
    public String getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(String groupNum) {
        this.groupNum = groupNum;
    }

    @Column(nullable = false)
    public Boolean getValidatePass() {
        return validatePass;
    }

    public void setValidatePass(Boolean validatePass) {
        this.validatePass = validatePass;
    }

    @Column(nullable = true, length = 2000)
    public String getValidateMessage() {
        return validateMessage;
    }

    public void setValidateMessage(String validateMessage) {
        this.validateMessage = validateMessage;
    }

    public Boolean getImportPass() {
        return importPass;
    }

    public void setImportPass(Boolean importPass) {
        this.importPass = importPass;
    }

    @Column(nullable = true, length = 2000)
    public String getImportMessage() {
        return importMessage;
    }

    public void setImportMessage(String importMessage) {
        this.importMessage = importMessage;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getCsdm() {
        return csdm;
    }

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }

    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    public String getGjdqm() {
        return gjdqm;
    }

    public void setGjdqm(String gjdqm) {
        this.gjdqm = gjdqm;
    }

    public String getSfzjlxm() {
        return sfzjlxm;
    }

    public void setSfzjlxm(String sfzjlxm) {
        this.sfzjlxm = sfzjlxm;
    }

    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    public String getGatqwm() {
        return gatqwm;
    }

    public void setGatqwm(String gatqwm) {
        this.gatqwm = gatqwm;
    }

    public String getZzmmm() {
        return zzmmm;
    }

    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }

    public String getJkzkm() {
        return jkzkm;
    }

    public void setJkzkm(String jkzkm) {
        this.jkzkm = jkzkm;
    }

    public String getGrbsm() {
        return grbsm;
    }

    public void setGrbsm(String grbsm) {
        this.grbsm = grbsm;
    }

    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    public @interface XlsMatchTitles {

        /**
         * (Optional) The name of the column. Defaults to
         * the property or field name.
         */
        String[] value() default "";

    }

    public void addValidateMessage(String msg) {
        if (validateMessage == null) {
            validateMessage = msg;
        } else {
            validateMessage = validateMessage + "\r\n" + msg;
        }
        validatePass = false;
    }

    public void addImportMessage(String msg) {
        if (importMessage == null) {
            importMessage = msg;
        } else {
            importMessage = importMessage + "\r\n" + msg;
        }
        importPass = false;
    }

    @Override
    public String toString() {
        return "XsXxImport [xh=" + xh + ", xm=" + xm + ", lineNum=" + lineNum + ", validateMessage=" + validateMessage
                + "]";
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getSsbj() {
        return ssbj;
    }

    public void setSsbj(String ssbj) {
        this.ssbj = ssbj;
    }
}
