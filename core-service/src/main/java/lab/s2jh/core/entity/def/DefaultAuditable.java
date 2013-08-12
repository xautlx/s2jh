package lab.s2jh.core.entity.def;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.domain.Persistable;

public interface DefaultAuditable<U, ID extends Serializable> extends Persistable<ID> {

    /**
     * Returns the user who created this entity.
     * 
     * @return the createdBy
     */
    U getCreatedBy();

    /**
     * Sets the user who created this entity.
     * 
     * @param createdBy the creating entity to set
     */
    void setCreatedBy(final U createdBy);

    /**
     * Returns the creation date of the entity.
     * 
     * @return the createdDate
     */
    Date getCreatedDate();

    /**
     * Sets the creation date of the entity.
     * 
     * @param creationDate the creation date to set
     */
    void setCreatedDate(final Date creationDate);

    /**
     * Returns the user who modified the entity lastly.
     * 
     * @return the lastModifiedBy
     */
    U getLastModifiedBy();

    /**
     * Sets the user who modified the entity lastly.
     * 
     * @param lastModifiedBy the last modifying entity to set
     */
    void setLastModifiedBy(final U lastModifiedBy);

    /**
     * Returns the date of the last modification.
     * 
     * @return the lastModifiedDate
     */
    Date getLastModifiedDate();

    /**
     * Sets the date of the last modification.
     * 
     * @param lastModifiedDate the date of the last modification to set
     */
    void setLastModifiedDate(final Date lastModifiedDate);
}
