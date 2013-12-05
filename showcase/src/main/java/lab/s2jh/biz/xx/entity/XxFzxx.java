package lab.s2jh.biz.xx.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "BIZ_XX_FZXX")
@MetaData(value = "学校辅助信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XxFzxx extends PersistableEntity<String> {

    @MetaData(value = "对应学校代码")
    private String xxdm;

    @MetaData(value = "对应学校")
    private XxJcxx xxJcxx;

    @MetaData(value = "组织机构码", description = "学校组织机构代码(中华人民共和国事业法人代码)，由八位本体代码和一位校验码组成")
    @EntityAutoCode(order = 10)
    private String zzjgm;

    @MetaData(value = "建校年月")
    @EntityAutoCode(order = 20)
    private String jxny;

    @MetaData(value = "校庆日")
    @EntityAutoCode(order = 30)
    private String xqr;

    @MetaData(value = "法人证书号")
    @EntityAutoCode(order = 40)
    private String frzsh;

    @MetaData(value = "土地产权码")
    @EntityAutoCode(order = 50)
    private String tdcqm;

    @MetaData(value = "土地证号")
    @EntityAutoCode(order = 60)
    private String tdzh;

    @MetaData(value = "附设班类型")
    @EntityAutoCode(order = 70)
    private String fsblxm;

    @MetaData(value = "学校校区数")
    @EntityAutoCode(order = 80)
    private Integer xxxqs;

    @MetaData(value = "学校（机构）评估类型")
    @EntityAutoCode(order = 90)
    private String xxpglxm;

    @MetaData(value = "学校评估情况说明 ")
    @EntityAutoCode(order = 100, listShow = false)
    private String xxpgqksm;

    @MetaData(value = "寄宿制形式码")
    @EntityAutoCode(order = 110)
    private String jszxsm;

    @MetaData(value = "学校海拔高度")
    @EntityAutoCode(order = 120)
    private BigDecimal hbgd;

    @MetaData(value = "党委负责人")
    @EntityAutoCode(order = 130)
    private String dwfzr;

    @MetaData(value = "学校经度")
    @EntityAutoCode(order = 140)
    private BigDecimal xxjd;

    @MetaData(value = "学校纬度")
    @EntityAutoCode(order = 150)
    private BigDecimal xxwd;

    @MetaData(value = "所在地地域属性")
    @EntityAutoCode(order = 160)
    private String szddysxm;

    @MetaData(value = "所在地经济属性码")
    @EntityAutoCode(order = 170)
    private String szdjjsxm;

    @MetaData(value = "所在地民族属性", description = "1 民族自治县;0 非民族自治县")
    @EntityAutoCode(order = 180)
    private String szdmzsx;

    @MetaData(value = "主教学语言码", description = "用三位数字代码")
    @EntityAutoCode(order = 190)
    private String zjxyym;

    @MetaData(value = "辅教学语言码", description = "用三位数字代码")
    @EntityAutoCode(order = 200)
    private String fjxyym;

    @MetaData(value = "招生半径", description = "招生的地区范围")
    @EntityAutoCode(order = 210)
    private String zsbj;

    @MetaData(value = "成立背景")
    @EntityAutoCode(order = 220)
    private String clbj;

    @MetaData(value = "历史沿革")
    @EntityAutoCode(order = 230)
    private String lsyg;

    @Transient
    public String getId() {
        return xxdm;
    }

    @Id
    @Column(name = "xxdm")
    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(final String xxdm) {
        this.xxdm = xxdm;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "xxdm", referencedColumnName = "xxsbm", insertable = false, updatable = false)
    public XxJcxx getXxJcxx() {
        return xxJcxx;
    }

    public void setXxJcxx(XxJcxx xxJcxx) {
        this.xxJcxx = xxJcxx;
    }

    @Override
    @Transient
    public String getDisplay() {
        return xxJcxx.getDisplay();
    }

    @Column(nullable = false, length = 10)
    public String getZzjgm() {
        return zzjgm;
    }

    public void setZzjgm(String zzjgm) {
        this.zzjgm = zzjgm;
    }

    @Column(nullable = true, length = 6)
    public String getJxny() {
        return jxny;
    }

    public void setJxny(String jxny) {
        this.jxny = jxny;
    }

    @Column(nullable = true, length = 60)
    public String getXqr() {
        return xqr;
    }

    public void setXqr(String xqr) {
        this.xqr = xqr;
    }

    @Column(nullable = false, length = 20)
    public String getFrzsh() {
        return frzsh;
    }

    public void setFrzsh(String frzsh) {
        this.frzsh = frzsh;
    }

    @Column(nullable = false, length = 1)
    public String getTdcqm() {
        return tdcqm;
    }

    public void setTdcqm(String tdcqm) {
        this.tdcqm = tdcqm;
    }

    @Column(nullable = false, length = 60)
    public String getTdzh() {
        return tdzh;
    }

    public void setTdzh(String tdzh) {
        this.tdzh = tdzh;
    }

    @Column(nullable = true, length = 1)
    public String getFsblxm() {
        return fsblxm;
    }

    public void setFsblxm(String fsblxm) {
        this.fsblxm = fsblxm;
    }

    public Integer getXxxqs() {
        return xxxqs;
    }

    public void setXxxqs(Integer xxxqs) {
        this.xxxqs = xxxqs;
    }

    @Column(nullable = false, length = 1)
    public String getXxpglxm() {
        return xxpglxm;
    }

    public void setXxpglxm(String xxpglxm) {
        this.xxpglxm = xxpglxm;
    }

    @Lob
    public String getXxpgqksm() {
        return xxpgqksm;
    }

    public void setXxpgqksm(String xxpgqksm) {
        this.xxpgqksm = xxpgqksm;
    }

    @Column(nullable = false, length = 1)
    public String getJszxsm() {
        return jszxsm;
    }

    public void setJszxsm(String jszxsm) {
        this.jszxsm = jszxsm;
    }

    @Column(nullable = true, precision = 18, scale = 2)
    public BigDecimal getHbgd() {
        return hbgd;
    }

    public void setHbgd(BigDecimal hbgd) {
        this.hbgd = hbgd;
    }

    @Column(nullable = false, length = 36)
    public String getDwfzr() {
        return dwfzr;
    }

    public void setDwfzr(String dwfzr) {
        this.dwfzr = dwfzr;
    }

    @Column(nullable = true, precision = 18, scale = 4)
    public BigDecimal getXxjd() {
        return xxjd;
    }

    public void setXxjd(BigDecimal xxjd) {
        this.xxjd = xxjd;
    }

    @Column(nullable = true, precision = 18, scale = 4)
    public BigDecimal getXxwd() {
        return xxwd;
    }

    public void setXxwd(BigDecimal xxwd) {
        this.xxwd = xxwd;
    }

    @Column(nullable = false, length = 1)
    public String getSzddysxm() {
        return szddysxm;
    }

    public void setSzddysxm(String szddysxm) {
        this.szddysxm = szddysxm;
    }

    @Column(nullable = false, length = 1)
    public String getSzdjjsxm() {
        return szdjjsxm;
    }

    public void setSzdjjsxm(String szdjjsxm) {
        this.szdjjsxm = szdjjsxm;
    }

    @Column(nullable = false, length = 1)
    public String getSzdmzsx() {
        return szdmzsx;
    }

    public void setSzdmzsx(String szdmzsx) {
        this.szdmzsx = szdmzsx;
    }

    @Column(nullable = false, length = 3)
    public String getZjxyym() {
        return zjxyym;
    }

    public void setZjxyym(String zjxyym) {
        this.zjxyym = zjxyym;
    }

    @Column(nullable = false, length = 3)
    public String getFjxyym() {
        return fjxyym;
    }

    public void setFjxyym(String fjxyym) {
        this.fjxyym = fjxyym;
    }

    @Column(nullable = true, length = 30)
    public String getZsbj() {
        return zsbj;
    }

    public void setZsbj(String zsbj) {
        this.zsbj = zsbj;
    }

    @Lob
    public String getClbj() {
        return clbj;
    }

    public void setClbj(String clbj) {
        this.clbj = clbj;
    }

    @Lob
    public String getLsyg() {
        return lsyg;
    }

    public void setLsyg(String lsyg) {
        this.lsyg = lsyg;
    }
}
