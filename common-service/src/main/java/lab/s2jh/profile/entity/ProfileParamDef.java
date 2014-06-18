package lab.s2jh.profile.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lab.s2jh.core.annotation.MetaData;
import lab.s2jh.core.entity.def.DynamicParameterDef;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "TBL_PROFILE_PARAM_DEF", uniqueConstraints = @UniqueConstraint(columnNames = { "CODE" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@MetaData(value = "个性化配置参数定义")
public class ProfileParamDef extends DynamicParameterDef {

    @Override
    @Transient
    public String getDisplay() {
        return getTitle();
    }

}
