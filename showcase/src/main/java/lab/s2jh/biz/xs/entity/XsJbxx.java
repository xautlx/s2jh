package lab.s2jh.biz.xs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.biz.sys.entity.EnumType.EnumTypes;
import lab.s2jh.biz.xs.imp.validation.EnumTypeValue;
import lab.s2jh.biz.xx.entity.XxBj;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BIZ_XS_JBXX", uniqueConstraints = { @UniqueConstraint(columnNames = { "xxdm", "xh" }) })
@MetaData(value = "学生个人基础信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class XsJbxx extends PersistableEntity<String> {

    @MetaData(value = "学校代码", description = "学生所属学校代码")
    @EntityAutoCode(order = 5, search = false, listShow = false)
    private String xxdm;

    @MetaData(value = "所属学校班级")
    @EntityAutoCode(order = 8, search = false, listShow = false, comparable = true)
    private XxBj xxBj;

    @MetaData(value = "学号", description = "学生在学校内编码,校内唯一")
    @EntityAutoCode(order = 10, search = true)
    private String xh;

    @MetaData(value = "姓名", description = "正式登记注册中文姓名,姓名为中文")
    @EntityAutoCode(order = 20, search = true)
    private String xm;

    @MetaData(value = "性别", description = "GB/T 2261.1 引用性别代码表（ZD_GB_XBM）性别：1-男，2-女")
    @EntityAutoCode(order = 30, searchAdvance = true)
    @EnumTypeValue(EnumTypes.ZD_GB_XBM)
    private String xbm;

    @MetaData(value = "出生日期", description = "出生证签署的并在公安户籍部门正式登记注册、人事档案中记载的时间日，格式：2011-01-01")
    @EntityAutoCode(order = 40, searchAdvance = true)
    private String csrq;

    @MetaData(value = "出生地", description = "引用行政区划表SYS_REGION_CODE的数据")
    @EntityAutoCode(order = 50, searchAdvance = true, listHidden = true)
    private String csdm;

    @MetaData(value = "籍贯")
    @EntityAutoCode(order = 60, searchAdvance = true, listHidden = true)
    private String jg;

    @MetaData(value = "民族", description = "GB/T 3304,取用2位数字代码，如：01 汉族，02 蒙古族 引用民族码代码表（ZD_GB_MZM）")
    @EntityAutoCode(order = 70, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_GB_MZM)
    private String mzm;

    @MetaData(value = "国籍/地区", description = "GB/T 2659。采用三字母代码，如：CHN 中国，USA 美国. 引用国籍地区码代码表（ZD_GB_GJDQM）")
    @EntityAutoCode(order = 80, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_GB_GJDQM)
    private String gjdqm;

    @MetaData(value = "身份证件类型", description = "可证明学生身份的证件类型：1-居民身份证；6-香港特区护照/身份证明；7-澳门特区护照/身份证明；8-台湾居民来往大陆通行证；9-境外永久居住证；引用身份证件类型代码表（ZD_BB_SFZJLXM）")
    @EntityAutoCode(order = 90, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_BB_SFZJLXM)
    private String sfzjlxm;

    @MetaData(value = "身份证件号", description = "身份证件类型对应的证件号码")
    @EntityAutoCode(order = 100, searchAdvance = true, listHidden = true)
    private String sfzjh;

    @MetaData(value = "港澳台侨外码", description = "CELTS-29 GATQW 港澳台侨外代码。引用港澳台侨外代码表（ZD_BB_GATQWM）")
    @EntityAutoCode(order = 110, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_BB_GATQWM)
    private String gatqwm;

    @MetaData(value = "政治面貌", description = "GB/T 4762,可采用简称，如：01 中共党员，04 民革会员。引用政治面貌代码表（ZD_BB_ZZMMM）")
    @EntityAutoCode(order = 120, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_BB_ZZMMM)
    private String zzmmm;

    @MetaData(value = "健康状况", description = "GB/T 2261.3,采用1位数字代码。引用健康状况代码表（ZD_GB_JKZKM）")
    @EntityAutoCode(order = 130, searchAdvance = true, listHidden = true)
    @EnumTypeValue(EnumTypes.ZD_GB_JKZKM)
    private String jkzkm;

    @MetaData(value = "照片", description = "近期正面免冠证件照片")
    @EntityAutoCode(order = 140, searchAdvance = true, listHidden = true, comparable = false)
    private byte[] zp;

    @MetaData(value = "个人标识码")
    @EntityAutoCode(order = 150, searchAdvance = true, listHidden = true)
    private String grbsm;

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    private XsFzxx xsFzxx;

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    private XsKzxx xsKzxx;

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    private XsLxxx xsLxxx;

    private String id;

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
        return xh + "/" + xm;
    }

    @Column(nullable = false, length = 20)
    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    @Column(nullable = false, length = 36)
    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    @Column(nullable = false, length = 1)
    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    @Column(nullable = false, length = 8)
    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    @Column(nullable = false, length = 6)
    public String getCsdm() {
        return csdm;
    }

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }

    @Column(nullable = true, length = 20)
    public String getJg() {
        return jg;
    }

    public void setJg(String jg) {
        this.jg = jg;
    }

    @Column(nullable = true, length = 2)
    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    @Column(nullable = false, length = 3)
    public String getGjdqm() {
        return gjdqm;
    }

    public void setGjdqm(String gjdqm) {
        this.gjdqm = gjdqm;
    }

    @Column(nullable = true, length = 1)
    public String getSfzjlxm() {
        return sfzjlxm;
    }

    public void setSfzjlxm(String sfzjlxm) {
        this.sfzjlxm = sfzjlxm;
    }

    @Column(nullable = true, length = 20)
    public String getSfzjh() {
        return sfzjh;
    }

    public void setSfzjh(String sfzjh) {
        this.sfzjh = sfzjh;
    }

    @Column(nullable = true, length = 2)
    public String getGatqwm() {
        return gatqwm;
    }

    public void setGatqwm(String gatqwm) {
        this.gatqwm = gatqwm;
    }

    @Column(nullable = false, length = 2)
    public String getZzmmm() {
        return zzmmm;
    }

    public void setZzmmm(String zzmmm) {
        this.zzmmm = zzmmm;
    }

    @Column(nullable = false, length = 1)
    public String getJkzkm() {
        return jkzkm;
    }

    public void setJkzkm(String jkzkm) {
        this.jkzkm = jkzkm;
    }

    @Lob
    public byte[] getZp() {
        return zp;
    }

    public void setZp(byte[] zp) {
        this.zp = zp;
    }

    @Column(nullable = true, length = 20)
    public String getGrbsm() {
        return grbsm;
    }

    public void setGrbsm(String grbsm) {
        this.grbsm = grbsm;
    }

    @Column(nullable = false, length = 10, updatable = false)
    public String getXxdm() {
        return xxdm;
    }

    @SkipParamBind
    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    @ManyToOne(optional = false)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "ssbj")
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    public XxBj getXxBj() {
        return xxBj;
    }

    public void setXxBj(XxBj xxBj) {
        this.xxBj = xxBj;
    }

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    @OneToOne(mappedBy = "xsJbxx", optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    public XsFzxx getXsFzxx() {
        return xsFzxx;
    }

    public void setXsFzxx(XsFzxx xsFzxx) {
        this.xsFzxx = xsFzxx;
    }

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    @OneToOne(mappedBy = "xsJbxx", optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    public XsKzxx getXsKzxx() {
        return xsKzxx;
    }

    public void setXsKzxx(XsKzxx xsKzxx) {
        this.xsKzxx = xsKzxx;
    }

    /**
     * 此处故意设置为optional = false,主要是Hack为了支持批量导出的Left Join Fetch查询
     * 实际关联对象有可能不存在,所以不能通过此属性方法获取对象的关联属性
     * 如果需要获取关联对象,请直接使用关联对象的查询接口
     */
    @OneToOne(mappedBy = "xsJbxx", optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    @Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
    public XsLxxx getXsLxxx() {
        return xsLxxx;
    }

    public void setXsLxxx(XsLxxx xsLxxx) {
        this.xsLxxx = xsLxxx;
    }
}
