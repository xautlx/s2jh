package lab.s2jh.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "TBL_AUTH_SIGNUP_USER")
@MetaData(value = "自助注册账号数据")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class SignupUser extends BaseUuidEntity {

    @MetaData(value = "用户唯一标识号")
    private String uid;

    @MetaData(value = "登录账号")
    @EntityAutoCode(order = 10, search = true)
    private String signinid;

    @MetaData(value = "登录密码")
    @EntityAutoCode(order = 10, listShow = false)
    private String password;

    @MetaData(value = "昵称")
    @EntityAutoCode(order = 20, search = true)
    private String nick;

    @MetaData(value = "电子邮件")
    @EntityAutoCode(order = 30, search = true)
    private String email;

    @MetaData(value = "注册时间")
    @EntityAutoCode(order = 40, edit = false, listHidden = true)
    private Date signupTime;

    @MetaData(value = "联系信息")
    @EntityAutoCode
    private String contactInfo;

    @MetaData(value = "备注说明")
    @EntityAutoCode
    private String remarkInfo;

    @MetaData(value = "审核处理时间")
    private Date auditTime;

    @Size(min = 3, max = 30)
    @Column(length = 128, unique = true, nullable = false)
    public String getSigninid() {
        return signinid;
    }

    public void setSigninid(String signinid) {
        this.signinid = signinid;
    }

    @Column(updatable = false, length = 128, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 64)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getSignupTime() {
        return signupTime;
    }

    @SkipParamBind
    public void setSignupTime(Date signupTime) {
        this.signupTime = signupTime;
    }

    @Email
    @Column(length = 128)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @Transient
    public String getDisplay() {
        return signinid;
    }

    @Column(length = 3000)
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Column(length = 3000)
    public String getRemarkInfo() {
        return remarkInfo;
    }

    public void setRemarkInfo(String remarkInfo) {
        this.remarkInfo = remarkInfo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Column(updatable = false, length = 64, unique = true, nullable = false)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
