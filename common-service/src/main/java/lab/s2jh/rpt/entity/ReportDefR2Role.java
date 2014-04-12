package lab.s2jh.rpt.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.auth.entity.Role;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tbl_RPT_REPORT_DEF_R2_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "REPORT_DEF_ID", "ROLE_ID" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "报表与角色关联")
public class ReportDefR2Role extends BaseUuidEntity {

    @MetaData(value = "所属报表")
    private ReportDef reportDef;

    @MetaData(value = "关联角色对象")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "REPORT_DEF_ID", nullable = false)
    public ReportDef getReportDef() {
        return reportDef;
    }

    public void setReportDef(ReportDef reportDef) {
        this.reportDef = reportDef;
    }

    @ManyToOne
    @JoinColumn(name = "ROLE_ID", nullable = false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Transient
    @Override
    public String getDisplay() {
        return role.getDisplay() + "_" + reportDef.getDisplay();
    }
}
