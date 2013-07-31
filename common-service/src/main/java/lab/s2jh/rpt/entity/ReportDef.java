package lab.s2jh.rpt.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.sys.entity.AttachmentFile;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@Table(name = "T_RPT_REPORT_DEF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "报表定义")
public class ReportDef extends BaseEntity<String> {

    @MetaData(title = "代码")
    @EntityAutoCode(order = 5, search = true)
    private String code;

    @MetaData(title = "名称", description = "此名称会作为报表下载文件名称")
    @EntityAutoCode(order = 10, search = true)
    private String title;

    @MetaData(title = "描述", description = " 对于报表用法的描述")
    @EntityAutoCode(order = 20, search = false, listShow = false)
    private String description;

    @MetaData(title = "类型")
    @EntityAutoCode(order = 30, search = true)
    private ReportTypeEnum type;

    @MetaData(title = "分类", description = "对于报表的分类，方便后续按类别显示")
    @EntityAutoCode(order = 40, search = true)
    private String category;

    @MetaData(title = "排序号", description = "用于在列表显示确定先后顺序")
    @EntityAutoCode(order = 60, search = false)
    private Integer orderRank = 100;

    @MetaData(title = "禁用标识", description = "禁用全局不显示")
    @EntityAutoCode(order = 70, search = true)
    private Boolean disabled = Boolean.FALSE;

    @MetaData(title = "模板文件ID")
    @EntityAutoCode(order = 100, search = false)
    private AttachmentFile templateFile;

    @MetaData(title = "关联的报表参数", description = "一般主要用于JasperReport类型报表,JXLS类型一般是在每个业务Action方法中特定组织参数对象给JXLS解析处理")
    private List<ReportParam> reportParameters;

    @MetaData(title = "角色关联")
    private List<ReportDefR2Role> reportDefR2Roles = Lists.newArrayList();

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

    public static enum ReportTypeEnum {

        /** 
         * 基于JXLS定制化Excel输出 
         * 在Excel模板中嵌入JXLS脚本代码，然后在各业务Action方法中组织想的参数传入JXLS上下文
         * 然后由JXLS负责转换生成输出的Excel文件
         */
        @MetaData(title = "Excel(JXLS)")
        EXCEL_JAVA,

        /** 
         * 基于VBS的Excel模板，一般是在Excel中直接嵌入报表处理逻辑，提供用户下载此Excel后运行Excel即可生成报表
         * 此模式基本是独立于应用实现的报表处理方式，完全由线下开发VBS脚本Excel文件即可 
         */
        @MetaData(title = "Excel(VBS)")
        EXCEL_VBS,

        /** 
         * 基于iReport设计的JasperReport模板文件
         * 一般提供有iReport编译完成的.jasper文件作为模板文件提供给JasperReport API解析处理
         */
        @MetaData(title = "JasperReport")
        JASPER;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return title;
    }

    @Column(nullable = false, unique = true, length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 128, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 1024, nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    public ReportTypeEnum getType() {
        return type;
    }

    public void setType(ReportTypeEnum type) {
        this.type = type;
    }

    @Column(length = 128, nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getOrderRank() {
        return orderRank;
    }

    public void setOrderRank(Integer orderRank) {
        this.orderRank = orderRank;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @OneToMany(mappedBy = "reportDef", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy(value = "orderRank desc")
    public List<ReportParam> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(List<ReportParam> reportParameters) {
        this.reportParameters = reportParameters;
    }

    @OneToMany(mappedBy = "reportDef", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<ReportDefR2Role> getReportDefR2Roles() {
        return reportDefR2Roles;
    }

    public void setReportDefR2Roles(List<ReportDefR2Role> reportDefR2Roles) {
        this.reportDefR2Roles = reportDefR2Roles;
    }

    @OneToOne
    @JoinColumn(name = "FILE_ID")
    public AttachmentFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(AttachmentFile templateFile) {
        this.templateFile = templateFile;
    }

    @Transient
    @JsonIgnore
    public String getReportAccessUrl() {
        return "/rpt/jasper-report!show?report=" + code;
    }
}
