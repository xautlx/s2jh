package lab.s2jh.profile.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.auth.entity.User;
import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.BaseUuidEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TBL_PROFILE_PARAM_DEF", uniqueConstraints = @UniqueConstraint(columnNames = { "CODE" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "个性化配置参数数据")
public class ProfileParamVal extends BaseUuidEntity {

    private User user;

    private ProfileParamDef profileParamDef;

    @Override
    @Transient
    public String getDisplay() {
        return user.getDisplay() + " " + profileParamDef.getDisplay();
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "PROFILE_PARAM_DEF_ID")
    @JsonIgnore
    public ProfileParamDef getProfileParamDef() {
        return profileParamDef;
    }

    public void setProfileParamDef(ProfileParamDef profileParamDef) {
        this.profileParamDef = profileParamDef;
    }

}
