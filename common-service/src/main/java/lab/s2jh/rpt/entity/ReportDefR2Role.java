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

import lab.s2jh.auth.entity.Role;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_RPT_REPORT_DEF_R2_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "REPORT_DEF_ID", "ROLE_ID" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(title = "报表与角色关联")
public class ReportDefR2Role extends BaseEntity<String> {

    @MetaData(title = "所属报表")
    private ReportDef reportDef;

    @MetaData(title = "关联角色对象")
    private Role role;

    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        if (id == null || StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            this.id = id;
        }
    }

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

    @Override
    @Transient
    public String getDisplayLabel() {
        return null;
    }
}
