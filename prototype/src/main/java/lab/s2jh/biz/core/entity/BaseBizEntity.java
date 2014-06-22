package lab.s2jh.biz.core.entity;

import javax.persistence.MappedSuperclass;

import lab.s2jh.core.entity.BaseNativeEntity;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@MappedSuperclass
@AuditOverrides({ @AuditOverride(forClass = BaseBizEntity.class) })
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public abstract class BaseBizEntity extends BaseNativeEntity {

}
