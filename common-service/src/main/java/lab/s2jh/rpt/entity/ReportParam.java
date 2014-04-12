package lab.s2jh.rpt.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.def.DynamicParameterDef;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_RPT_REPORT_PARAM", uniqueConstraints = @UniqueConstraint(columnNames = { "REPORT_DEF_ID", "CODE" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "报表参数")
public class ReportParam extends DynamicParameterDef {

    @MetaData(value = "所属报表")
    private ReportDef reportDef;

    @Override
    @Transient
    public String getDisplay() {
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
