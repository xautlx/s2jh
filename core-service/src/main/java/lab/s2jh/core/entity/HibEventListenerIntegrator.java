package lab.s2jh.core.entity;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibEventListenerIntegrator implements Integrator {

    private static final Logger logger = LoggerFactory.getLogger(HibEventListenerIntegrator.class);

    @Override
    public void integrate(Configuration configuration, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        //        EventListenerRegistry listenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
        //        listenerRegistry.addDuplicationStrategy( EnversListenerDuplicationStrategy.INSTANCE );
        //        Iterator<PersistentClass> classMappings = configuration.getClassMappings();
        //        while (classMappings.hasNext()) {
        //            PersistentClass persistentClass = classMappings.next();
        //            Class entityClass = persistentClass.getMappedClass();
        //            HibEventListeners listeners = (HibEventListeners) entityClass.getAnnotation(HibEventListeners.class);
        //            if (listeners != null && listeners.value() != null) {
        //                for(Class listener:listeners.value()){
        //                    
        //                }
        //            }
        //            logger.debug("---------------" + persistentClass.getMappedClass());
        //        }
    }

    @Override
    public void integrate(MetadataImplementor metadata, SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        // TODO Auto-generated method stub

    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // TODO Auto-generated method stub

    }

}
