package lab.s2jh.biz.xs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BIZ_XS_LXXX")
@MetaData(title = "学生个人联系信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XsLxxx extends PersistableEntity<String> {
    
    private XsJbxx xsJbxx;

    @MetaData(title = "学号", description = "学生在学校内编码")
    @EntityAutoCode(order = 10, search = true)
    private String xh;
    
    @MetaData(title = "现地址", description = "指本人的常住地址")
    @EntityAutoCode(order = 20, search = true)
    private String xzz;
    
    @MetaData(title = "邮政编码", description = "指家庭的常住地址的邮政编码")
    @EntityAutoCode(order = 30)
    private String yzbm;

    @MetaData(title = "联系电话")
    @EntityAutoCode(order = 40)
    private String lxdh;
    
    @MetaData(title = "通信地址",description="指包括省（自治区、直辖市）/地（市、州）/县（区、旗）/乡（镇）/街（村）/门牌")
    @EntityAutoCode(order = 50)
    private String txdz;
    
    @MetaData(title = "电子信箱",description="学生在互联网上的电子邮件信箱地址")
    @EntityAutoCode(order = 60)
    private String dzxx;
    
    @MetaData(title = "主页地址",description="学生在互联网上的个人主页地址")
    @EntityAutoCode(order = 70)
    private String zydz;
    
    @MetaData(title = "即时通讯号")
    @EntityAutoCode(order = 80)
    private String jstxh;

    private String id;
    
    /**
     * 基类采用通过ID判定是否新实例,由于此对象采用Assigned主键策略
     * 因此覆写采用关联对象来进行判定
     */
    @Override
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return xsJbxx == null;
    }

    @Id
    @Column(length = 40)
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return xh;
    }

    @Column(nullable = false, length = 20)
    public String getXh() {
        return xh;
    }

    @SkipParamBind
    public void setXh(String xh) {
        this.xh = xh;
    }

    @Column(nullable = true, length = 180)
    public String getXzz() {
        return xzz;
    }

    public void setXzz(String xzz) {
        this.xzz = xzz;
    }

    @Column(nullable = true, length = 6)
    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    @Column(nullable = true, length = 30)
    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    @Column(nullable = true, length = 180)
    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    @Column(nullable = true, length = 40)
    public String getDzxx() {
        return dzxx;
    }

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    @Column(nullable = true, length = 60)
    public String getZydz() {
        return zydz;
    }

    public void setZydz(String zydz) {
        this.zydz = zydz;
    }

    @Column(nullable = true, length = 40)
    public String getJstxh() {
        return jstxh;
    }

    public void setJstxh(String jstxh) {
        this.jstxh = jstxh;
    }


    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", insertable = false, updatable = false, nullable = false)
    @JsonIgnore
    public XsJbxx getXsJbxx() {
        return xsJbxx;
    }

    public void setXsJbxx(XsJbxx xsJbxx) {
        this.xsJbxx = xsJbxx;
    }
}
