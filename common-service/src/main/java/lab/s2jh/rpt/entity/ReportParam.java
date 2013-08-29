package lab.s2jh.rpt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.def.DynamicParameterDef;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_RPT_REPORT_PARAM", uniqueConstraints = @UniqueConstraint(columnNames = { "REPORT_DEF_ID", "CODE" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "报表参数")
public class ReportParam extends DynamicParameterDef {

    @MetaData(title = "所属报表")
    private ReportDef reportDef;

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
    public String getDisplayLabel() {
        return getTitle();
    }

    @ManyToOne
    @JoinColumn(name = "REPORT_DEF_ID")
    @JsonIgnore
    public ReportDef getReportDef() {
        return reportDef;
    }

    public void setReportDef(ReportDef reportDef) {
        this.reportDef = reportDef;
    }
}
