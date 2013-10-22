package lab.s2jh.biz.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "SYS_REGION_CODE")
@MetaData(value = "行政区划 ")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class RegionCode extends PersistableEntity<String> {

    @MetaData(value = "行政区划代码", description = "必须以ROLE_打头")
    @EntityAutoCode(order = 10, search = true)
    private String regionCode;

    @MetaData(value = "行政区划名称")
    @EntityAutoCode(order = 20, search = true)
    private String regionDesc;

    @MetaData(value = "行政区划简称")
    @EntityAutoCode(order = 30, search = true)
    private String regionShort;

    @MetaData(value = "教育行政部门名称")
    @EntityAutoCode(order = 40, searchAdvance = false)
    private String regionEdu;

    @MetaData(value = "父行政区划代码", description = "对应该表的region_code字段")
    @EntityAutoCode(order = 50, searchAdvance = false)
    private String parentcode;

    @MetaData(value = "地区类型", description = "引用地区类型代码表（zd_xt_dqlx）1：西部地区 2：中部地区 3：东部地区")
    @EntityAutoCode(order = 60, searchAdvance = true)
    private String dqlx;

    @MetaData(value = "可用状态", description = "1：可用  0：不可用")
    @EntityAutoCode(order = 70, search = true)
    private Boolean enabled = Boolean.TRUE;

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

    @Column(nullable = false, length = 60, unique = true)
    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @Column(nullable = false, length = 100)
    public String getRegionDesc() {
        return regionDesc;
    }

    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }

    @Column(nullable = true, length = 100)
    public String getRegionShort() {
        return regionShort;
    }

    public void setRegionShort(String regionShort) {
        this.regionShort = regionShort;
    }

    @Column(nullable = true, length = 100)
    public String getRegionEdu() {
        return regionEdu;
    }

    public void setRegionEdu(String regionEdu) {
        this.regionEdu = regionEdu;
    }

    @Column(nullable = true, length = 60)
    public String getParentcode() {
        return parentcode;
    }

    public void setParentcode(String parentcode) {
        this.parentcode = parentcode;
    }

    @Column(nullable = true, length = 10)
    public String getDqlx() {
        return dqlx;
    }

    public void setDqlx(String dqlx) {
        this.dqlx = dqlx;
    }

    @Type(type = "lab.s2jh.biz.core.hib.BooleanStrUserType")
    @Column(nullable = false, length = 1, name = "status")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return this.regionDesc;
    }

    @Transient
    public static boolean isProvice(String regionCode) {
        if (regionCode != null && regionCode.length() == 6 && regionCode.endsWith("0000")) {
            return true;
        }
        return false;
    }

    @Transient
    public static boolean isCity(String regionCode) {
        if (regionCode != null && regionCode.length() == 6 && regionCode.endsWith("00") && !regionCode.endsWith("0000")) {
            return true;
        }
        return false;
    }
}
