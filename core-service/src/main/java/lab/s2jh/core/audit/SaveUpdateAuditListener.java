package lab.s2jh.core.audit;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lab.s2jh.core.entity.def.DefaultAuditable;
import lab.s2jh.core.security.AuthContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

/**
 * 审计记录记录创建和修改信息
 * @see AuditingEntityListener
 *
 */
@Component
public class SaveUpdateAuditListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private boolean dateTimeForNow = true;
    private boolean modifyOnCreation = false;

    public void setDateTimeForNow(boolean dateTimeForNow) {
        this.dateTimeForNow = dateTimeForNow;
    }

    public void setModifyOnCreation(final boolean modifyOnCreation) {
        this.modifyOnCreation = modifyOnCreation;
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on persist events.
     * 
     * @param target
     */
    @PrePersist
    public void touchForCreate(Object target) {
        touch(target, true);
    }

    /**
     * Sets modification and creation date and auditor on the target object in
     * case it implements {@link DefaultAuditable} on update events.
     * 
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(Object target) {

        touch(target, false);
    }

    private void touch(Object target, boolean isNew) {

        if (!(target instanceof DefaultAuditable)) {
            return;
        }

        @SuppressWarnings("unchecked")
        DefaultAuditable<String, ?> auditable = (DefaultAuditable<String, ?>) target;

        String auditor = touchAuditor(auditable, isNew);
        Date now = dateTimeForNow ? touchDate(auditable, isNew) : null;

        Object defaultedNow = now == null ? "not set" : now;
        Object defaultedAuditor = auditor == null ? "unknown" : auditor;

        logger.debug("Touched {} - Last modification at {} by {}", new Object[] { auditable, defaultedNow,
                defaultedAuditor });
    }

    /**
     * Sets modifying and creating auditioner. Creating auditioner is only set
     * on new auditables.
     * 
     * @param auditable
     * @return
     */
    private String touchAuditor(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        String auditor = AuthContextHolder.getAuthUserPin();

        if (isNew) {

            auditable.setCreatedBy(auditor);

            if (!modifyOnCreation) {
                return auditor;
            }
        }

        auditable.setLastModifiedBy(auditor);

        return auditor;
    }

    /**
     * Touches the auditable regarding modification and creation date. Creation
     * date is only set on new auditables.
     * 
     * @param auditable
     * @return
     */
    private Date touchDate(final DefaultAuditable<String, ?> auditable, boolean isNew) {

        Date now = new Date();

        if (isNew) {
            auditable.setCreatedDate(now);

            if (!modifyOnCreation) {
                return now;
            }
        }

        auditable.setLastModifiedDate(now);

        return now;
    }
}
