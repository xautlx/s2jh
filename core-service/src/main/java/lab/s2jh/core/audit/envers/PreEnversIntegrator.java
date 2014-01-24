package lab.s2jh.core.audit.envers;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversIntegrator;
import org.hibernate.envers.event.EnversListenerDuplicationStrategy;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.classloading.spi.ClassLoaderService;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.jboss.logging.Logger;

public class PreEnversIntegrator implements Integrator {
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
            EnversIntegrator.class.getName());

    public static final String AUTO_REGISTER = "hibernate.listeners.envers.autoRegister";

    @Override
    public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        final boolean autoRegister = ConfigurationHelper.getBoolean(AUTO_REGISTER, configuration.getProperties(), true);
        if (!autoRegister) {
            LOG.debug("Skipping Envers listener auto registration");
            return;
        }

        EventListenerRegistry listenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
        listenerRegistry.addDuplicationStrategy(EnversListenerDuplicationStrategy.INSTANCE);

        final AuditConfiguration enversConfiguration = AuditConfiguration.getFor(configuration,
                serviceRegistry.getService(ClassLoaderService.class));

        if (enversConfiguration.getEntCfg().hasAuditedEntities()) {

            listenerRegistry.appendListeners(EventType.PRE_INSERT, new EnversPreInsertEventListenerImpl(
                    enversConfiguration));
            listenerRegistry.appendListeners(EventType.PRE_UPDATE, new EnversPreUpdateEventListenerImpl(
                    enversConfiguration));
        }
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // nothing to do afaik
    }

    /**
     * {@inheritDoc}
     *
     * @see org.hibernate.integrator.spi.Integrator#integrate(org.hibernate.metamodel.source.MetadataImplementor, org.hibernate.engine.spi.SessionFactoryImplementor, org.hibernate.service.spi.SessionFactoryServiceRegistry)
     */
    @Override
    public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        // TODO: implement
    }

}
