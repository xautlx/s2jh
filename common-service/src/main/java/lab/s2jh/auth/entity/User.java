package lab.s2jh.auth.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.entity.annotation.SkipParamBind;
import lab.s2jh.core.web.json.DateJsonSerializer;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

@Entity
@Table(name = "T_AUTH_USER", uniqueConstraints = @UniqueConstraint(columnNames = { "aclCode", "signinid" }))
@MetaData(title = "用户")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Audited
public class User extends BaseEntity<String> {

    @MetaData(title = "登录账号")
    @EntityAutoCode(order = 10, search = true)
    private String signinid;

    @MetaData(title = "登录密码")
    @EntityAutoCode(order = 10, listShow = false)
    private String password;

    @MetaData(title = "昵称")
    @EntityAutoCode(order = 20, search = true)
    private String nick;

    @MetaData(title = "电子邮件")
    @EntityAutoCode(order = 30, search = true)
    private String email;

    @MetaData(title = "禁用标识", description = "禁用之后则不能登录访问系统")
    @EntityAutoCode(order = 40, search = true)
    private Boolean disabled = Boolean.FALSE;

    @MetaData(title = "注册时间")
    @EntityAutoCode(order = 40, edit = false, listHidden = true)
    private Date signupTime;

    @MetaData(title = "账户未锁定标志")
    @EntityAutoCode(order = 50, search = false, listHidden = true)
    private Boolean accountNonLocked = Boolean.TRUE;

    @MetaData(title = "失效日期")
    @EntityAutoCode(order = 50, search = true)
    private Date accountExpireTime;

    @MetaData(title = "账户密码过期时间")
    @EntityAutoCode(order = 50, search = false, listHidden = true)
    private Date credentialsExpireTime;

    @MetaData(title = "角色关联")
    private List<UserR2Role> userR2Roles = Lists.newArrayList();

    @MetaData(title = "初始化用户标识")
    private Boolean initSetupUser;

    @MetaData(title = "用户唯一标识号")
    private String uid;

    private Date lastLogonTime;

    private String lastLogonIP;

    private String lastLogonHost;
    
    private Long logonTimes;
    
    private String id;

    @Id
    @Column(length = 40)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        //容错处理id以空字符提交参数时修改为null避免hibernate主键无效修改
        if (id == null || StringUtils.isBlank(id)) {
            this.id = null;
        } else {
            this.id = id;
        }
    }

    @Size(min = 3, max = 30)
    @Column(length = 128, unique = true, nullable = false, updatable = false)
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

    @Column(nullable = false)
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        return (this.getAclCode() == null ? "" : this.getAclCode() + "/") + signinid
                + (this.getNick() == null ? "" : "/" + this.getNick());
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

    @Column(updatable = false, length = 32, unique = true, nullable = false, name = "USER_ID")
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
}
