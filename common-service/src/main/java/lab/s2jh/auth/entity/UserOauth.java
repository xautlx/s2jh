package lab.s2jh.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_AUTH_USER_OAUTH")
@MetaData(title = "用户绑定OAUTH认证")
public class UserOauth extends BaseEntity<String> {

    private User user;
    private String providerUid;
    private String providerType;
    private Date bindTime;

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

    @Column(length = 128, nullable = false)
    public String getProviderUid() {
        return providerUid;
    }

    public void setProviderUid(String providerUid) {
        this.providerUid = providerUid;
    }

    @Column(length = 128, nullable = false)
    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    @Transient
    public String getDisplayLabel() {
        // TODO Auto-generated method stub
        return null;
    }

}
