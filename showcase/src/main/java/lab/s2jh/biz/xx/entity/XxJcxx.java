package lab.s2jh.biz.xx.entity;

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

@Entity
@Table(name = "BIZ_XX_JCXX")
@MetaData(value = "学校基本信息")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class XxJcxx extends PersistableEntity<String> {

    @MetaData(value = "学校识别码", description = "指由教育部按照国家标准及编码规则统一编制，赋予每一个学校（机构）在全国范围内唯一的、始终不变的识别标识码。")
    @EntityAutoCode(order = 10, search = true)
    private String xxdm;

    @MetaData(value = "学校(机构)代码", description = "采用事业统计中完整的学校（机构）代码")
    @EntityAutoCode(order = 20, listHidden = true)
    private String xxjgdm;

    @MetaData(value = "学校名称")
    @EntityAutoCode(order = 30, search = true)
    private String xxmc;

    @MetaData(value = "学校英文名称")
    @EntityAutoCode(order = 40, listHidden = true)
    private String xxywmc;

    @MetaData(value = "行政区划码")
    @EntityAutoCode(order = 50)
    private String xzqhm;

    @MetaData(value = "学校地址")
    @EntityAutoCode(order = 60, listShow = false)
    private String xxdz;

    @MetaData(value = "学校邮政编码")
    @EntityAutoCode(order = 70, listHidden = true)
    private String xxyzbm;

    @MetaData(value = "属地管理教育行政部门代码")
    @EntityAutoCode(order = 80, listHidden = true)
    private String sdgljyxzbm;

    @MetaData(value = "学校所属主管教育行政部门代码")
    @EntityAutoCode(order = 90, listHidden = true)
    private String sszgdwm;

    @MetaData(value = "举办者代码", description = "引用学校（教育机构）举办者代码表（ZD_BB_XXJYJGJBZ)1/2/3/4/5/6/7 中央党政机关、人民团体及其他机构引用 GB/T 4657")
    @EntityAutoCode(order = 100, listHidden = true)
    private String xxjbzm;

    @MetaData(value = "办学类型代码", description = "引用办学类型代码表（ZD_BB_BXLX）")
    @EntityAutoCode(order = 110, listHidden = true)
    private String xxbxlxm;

    @MetaData(value = "城乡分类代码", description = "CELTS-29 SZDCXLX 所在地城乡类型代码")
    @EntityAutoCode(order = 120, listHidden = true)
    private String szdcxlxm;

    @MetaData(value = "学校举办者类别码", description = "CELTS-29 XXJYJGJBZ 学校（教育机构）举办者代码")
    @EntityAutoCode(order = 130, listHidden = true)
    private String xxbbm2;

    @MetaData(value = "校长姓名")
    @EntityAutoCode(order = 140)
    private String xzxm;

    @MetaData(value = "校长手机号码")
    @EntityAutoCode(order = 150)
    private String xzsjhm;

    @MetaData(value = "联系电话")
    @EntityAutoCode(order = 170)
    private String lxdh;

    @MetaData(value = "传真电话")
    @EntityAutoCode(order = 180, listHidden = true)
    private String czdh;

    @MetaData(value = "电子信箱")
    @EntityAutoCode(order = 190)
    private String dzxx;

    @MetaData(value = "主页地址")
    @EntityAutoCode(order = 200, listHidden = true)
    private String zydz;

    @MetaData(value = "学校办别码", description = "CELTS-29 XXBB 学校办别代码")
    @EntityAutoCode(order = 210, listHidden = true)
    private String xxbbm;

    @MetaData(value = "学校辅助信息")
    private XxFzxx xxFzxx;

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

    @Column(nullable = false, length = 10, unique = true, name = "xxsbm")
    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    @Column(nullable = false, length = 200)
    public String getXxjgdm() {
        return xxjgdm;
    }

    public void setXxjgdm(String xxjgdm) {
        this.xxjgdm = xxjgdm;
    }

    @Column(nullable = false, length = 60)
    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    @Column(nullable = true, length = 180)
    public String getXxywmc() {
        return xxywmc;
    }

    public void setXxywmc(String xxywmc) {
        this.xxywmc = xxywmc;
    }

    @Column(nullable = false, length = 6)
    public String getXzqhm() {
        return xzqhm;
    }

    public void setXzqhm(String xzqhm) {
        this.xzqhm = xzqhm;
    }

    @Column(nullable = false, length = 180)
    public String getXxdz() {
        return xxdz;
    }

    public void setXxdz(String xxdz) {
        this.xxdz = xxdz;
    }

    @Column(nullable = false, length = 6)
    public String getXxyzbm() {
        return xxyzbm;
    }

    public void setXxyzbm(String xxyzbm) {
        this.xxyzbm = xxyzbm;
    }

    @Column(nullable = false, length = 12)
    public String getSdgljyxzbm() {
        return sdgljyxzbm;
    }

    public void setSdgljyxzbm(String sdgljyxzbm) {
        this.sdgljyxzbm = sdgljyxzbm;
    }

    @Column(nullable = true, length = 6)
    public String getSszgdwm() {
        return sszgdwm;
    }

    public void setSszgdwm(String sszgdwm) {
        this.sszgdwm = sszgdwm;
    }

    @Column(nullable = false, length = 3)
    public String getXxjbzm() {
        return xxjbzm;
    }

    public void setXxjbzm(String xxjbzm) {
        this.xxjbzm = xxjbzm;
    }

    @Column(nullable = false, length = 3)
    public String getXxbxlxm() {
        return xxbxlxm;
    }

    public void setXxbxlxm(String xxbxlxm) {
        this.xxbxlxm = xxbxlxm;
    }

    @Column(nullable = false, length = 3)
    public String getSzdcxlxm() {
        return szdcxlxm;
    }

    public void setSzdcxlxm(String szdcxlxm) {
        this.szdcxlxm = szdcxlxm;
    }

    @Column(nullable = false, length = 3)
    public String getXxbbm2() {
        return xxbbm2;
    }

    public void setXxbbm2(String xxbbm2) {
        this.xxbbm2 = xxbbm2;
    }

    @Column(nullable = false, length = 36)
    public String getXzxm() {
        return xzxm;
    }

    public void setXzxm(String xzxm) {
        this.xzxm = xzxm;
    }

    @Column(nullable = false, length = 30)
    public String getXzsjhm() {
        return xzsjhm;
    }

    public void setXzsjhm(String xzsjhm) {
        this.xzsjhm = xzsjhm;
    }

    @Column(nullable = false, length = 30)
    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    @Column(nullable = true, length = 30)
    public String getCzdh() {
        return czdh;
    }

    public void setCzdh(String czdh) {
        this.czdh = czdh;
    }

    @Column(nullable = false, length = 40)
    public String getDzxx() {
        return dzxx;
    }

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    @Column(nullable = false, length = 60)
    public String getZydz() {
        return zydz;
    }

    public void setZydz(String zydz) {
        this.zydz = zydz;
    }

    @Column(nullable = false, length = 6)
    public String getXxbbm() {
        return xxbbm;
    }

    public void setXxbbm(String xxbbm) {
        this.xxbbm = xxbbm;
    }

    @Override
    @Transient
    public String getDisplay() {
        return xxdm + "/" + xxmc;
    }
}
