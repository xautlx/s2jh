package ${root_package}.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lab.apollo.core.entity.AbstractEntity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Auto Generate Javadoc
 */
@Entity
@Table(name = "${table_name}")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ${entity_name} extends BaseEntity<String> {

}