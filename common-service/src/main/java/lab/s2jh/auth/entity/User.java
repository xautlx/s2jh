package lab.s2jh.auth.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

@Entity
@Table(name = "T_AUTH_USER")
@MetaData(value = "用户")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class User extends BaseEntity<Long> {

    public final static String[] PROTECTED_USER_NAMES = new String[] { "admin", "super" };

    @MetaData(value = "所属部门")
    private Department department;

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

    @MetaData(value = "启用标识", description = "禁用之后则不能登录访问系统")
    @EntityAutoCode(order = 40, search = true)
    private Boolean enabled = Boolean.TRUE;

    @MetaData(value = "注册时间")
    @EntityAutoCode(order = 40, edit = false, listHidden = true)
    private Date signupTime;

    @MetaData(value = "账户未锁定标志")
    @EntityAutoCode(order = 50, search = false, listHidden = true)
    private Boolean accountNonLocked = Boolean.TRUE;

    @MetaData(value = "失效日期")
    @EntityAutoCode(order = 50, search = true)
    private Date accountExpireTime;

    @MetaData(value = "账户密码过期时间")
    @EntityAutoCode(order = 50, search = false, listHidden = true)
    private Date credentialsExpireTime;

    @MetaData(value = "角色关联")
    private List<UserR2Role> userR2Roles = Lists.newArrayList();

    @MetaData(value = "初始化用户标识")
    private Boolean initSetupUser;

    @MetaData(value = "用户唯一标识号")
    private String uid;

    @MetaData(value = "最后登录时间")
    private Date lastLogonTime;

    @MetaData(value = "最后登录IP")
    private String lastLogonIP;

    @MetaData(value = "最后登录主机名")
    private String lastLogonHost;

    @MetaData(value = "总计登录次数")
    private Long logonTimes;

    private Long id;

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "native")
    @Column(name = "sid")
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Size(min = 3, max = 30)
    @Column(length = 128, unique = true, nullable = false, updatable = false, name = "user_id")
    public String getSigninid() {
        return signinid;
    }

    public void setSigninid(String signinid) {
        this.signinid = signinid;
    }

    @Column(length = 128)
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

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateJsonSerializer.class)
    public Date getAccountExpireTime() {
        return accountExpireTime;
    }

    public void setAccountExpireTime(Date accountExpireTime) {
        this.accountExpireTime = accountExpireTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateJsonSerializer.class)
    public Date getCredentialsExpireTime() {
        return credentialsExpireTime;
    }

    public void setCredentialsExpireTime(Date credentialsExpireTime) {
        this.credentialsExpireTime = credentialsExpireTime;
    }

    @Type(type = "yes_no")
    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    @Transient
    public String getDisplay() {
        return (this.getAclCode() == null ? "" : this.getAclCode() + " ") + signinid
                + (this.getNick() == null ? "" : " " + this.getNick());
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    @JsonIgnore
    public List<UserR2Role> getUserR2Roles() {
        return userR2Roles;
    }

    public void setUserR2Roles(List<UserR2Role> userR2Roles) {
        this.userR2Roles = userR2Roles;
    }

    @Column(updatable = false)
    public Boolean getInitSetupUser() {
        return initSetupUser;
    }

    public void setInitSetupUser(Boolean initSetupUser) {
        this.initSetupUser = initSetupUser;
    }

    @Column(updatable = false, length = 64, unique = true, nullable = false)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getLastLogonTime() {
        return lastLogonTime;
    }

    public void setLastLogonTime(Date lastLogonTime) {
        this.lastLogonTime = lastLogonTime;
    }

    @Column(length = 128, nullable = true)
    public String getLastLogonIP() {
        return lastLogonIP;
    }

    public void setLastLogonIP(String lastLogonIP) {
        this.lastLogonIP = lastLogonIP;
    }

    @Column(length = 128, nullable = true)
    public String getLastLogonHost() {
        return lastLogonHost;
    }

    public void setLastLogonHost(String lastLogonHost) {
        this.lastLogonHost = lastLogonHost;
    }

    public Long getLogonTimes() {
        return logonTimes;
    }

    public void setLogonTimes(Long logonTimes) {
        this.logonTimes = logonTimes;
    }

    @Type(type = "yes_no")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
