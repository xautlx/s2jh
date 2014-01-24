package lab.s2jh.core.audit.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.BaseEnversEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnversPreUpdateEventListenerImpl extends BaseEnversEventListener implements PreUpdateEventListener {

    private static Logger logger = LoggerFactory.getLogger(EnversPreUpdateEventListenerImpl.class);

    public EnversPreUpdateEventListenerImpl(AuditConfiguration enversConfiguration) {
        super(enversConfiguration);
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        String entityName = event.getPersister().getEntityName();
        if (getAuditConfiguration().getEntCfg().isVersioned(entityName)) {
            logger.debug("Envers onPreUpdate event.getEntity()={}", event.getEntity());
        }

        return false;
    }
}
