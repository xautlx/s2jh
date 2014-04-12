package lab.s2jh.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseNativeEntity;
import lab.s2jh.core.entity.annotation.EntityAutoCode;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.web.json.DateTimeJsonSerializer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "tbl_AUTH_LOGON_LOG")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@MetaData(value = "用户登录登出历史记录")
public class UserLogonLog extends BaseNativeEntity {

    @MetaData(value = "登录账号")
    @EntityAutoCode(order = 10, search = true)
    private String username;

    @MetaData(value = "账户编号")
    @EntityAutoCode(order = 20, search = false, listHidden = true)
    private String userid;

    @MetaData(value = "登录时间")
    @EntityAutoCode(order = 22, search = true)
    private Date logonTime;

    @MetaData(value = "登出时间")
    @EntityAutoCode(order = 24, search = false)
    private Date logoutTime;

    @MetaData(value = "登录时长")
    @EntityAutoCode(order = 28, search = false)
    private Long logonTimeLength;

    @MetaData(value = "登录次数")
    @EntityAutoCode(order = 30, search = false)
    private Long logonTimes;

    @MetaData(value = "userAgent")
    @EntityAutoCode(order = 32, search = false, listHidden = true)
    private String userAgent;

    @MetaData(value = "xforwardFor")
    @EntityAutoCode(order = 36, search = false)
    private String xforwardFor;

    @MetaData(value = "localAddr")
    @EntityAutoCode(order = 40, search = false, listHidden = true)
    private String localAddr;

    @MetaData(value = "localName")
    @EntityAutoCode(order = 50, search = false, listHidden = true)
    private String localName;

    @MetaData(value = "localPort")
    @EntityAutoCode(order = 60, search = false, listHidden = true)
    private Integer localPort;

    @MetaData(value = "remoteAddr")
    @EntityAutoCode(order = 70, search = false, listHidden = true)
    private String remoteAddr;

    @MetaData(value = "remoteHost")
    @EntityAutoCode(order = 80, search = false, listHidden = true)
    private String remoteHost;

    @MetaData(value = "remotePort")
    @EntityAutoCode(order = 90, search = false, listHidden = true)
    private Integer remotePort;

    @MetaData(value = "serverIP")
    @EntityAutoCode(order = 100, search = false, listHidden = true)
    private String serverIP;

    @MetaData(value = "Session编号")
    @EntityAutoCode(order = 160, search = false, listHidden = true)
    private String httpSessionId;

    @Column(length = 128, nullable = false, unique = true)
    public String getHttpSessionId() {
        return httpSessionId;
    }

    public void setHttpSessionId(String httpSessionId) {
        this.httpSessionId = httpSessionId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getLogonTime() {
        return logonTime;
    }

    public void setLogonTime(Date logonTime) {
        this.logonTime = logonTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Column(length = 1024, nullable = true)
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getLogonTimeLength() {
        return logonTimeLength;
    }

    public void setLogonTimeLength(Long logonTimeLength) {
        this.logonTimeLength = logonTimeLength;
    }

    @Column(length = 1024, nullable = true)
    public String getXforwardFor() {
        return xforwardFor;
    }

    public void setXforwardFor(String xforwardFor) {
        this.xforwardFor = xforwardFor;
    }

    @Column(length = 128, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 128, nullable = false)
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Column(length = 128, nullable = true)
    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    @Column(length = 128, nullable = true)
    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    @Column(length = 128, nullable = true)
    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Column(length = 128, nullable = true)
    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    @Column(length = 128, nullable = true)
    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public Long getLogonTimes() {
        return logonTimes;
    }

    public void setLogonTimes(Long logonTimes) {
        this.logonTimes = logonTimes;
    }

    @Override
    @Transient
    public String getDisplay() {
        return username;
    }

    @Transient
    public String getLogonTimeLengthFriendly() {
        return DateUtils.getHumanDisplayForTimediff(logonTimeLength);
    }
}