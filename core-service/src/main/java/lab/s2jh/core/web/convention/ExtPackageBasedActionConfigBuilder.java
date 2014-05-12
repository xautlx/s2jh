package lab.s2jh.core.web.convention;

import org.apache.struts2.convention.PackageBasedActionConfigBuilder;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

/**
 * 简单扩展Convention Plugin加载入口，以便更好的兼容以tomcat\lib共享jar部署模式
 */
public class ExtPackageBasedActionConfigBuilder extends PackageBasedActionConfigBuilder {

    @Inject
    public ExtPackageBasedActionConfigBuilder(Configuration configuration, Container container,
            ObjectFactory objectFactory, @Inject("struts.convention.redirect.to.slash") String redirectToSlash,
            @Inject("struts.convention.default.parent.package") String defaultParentPackage) {
        super(configuration, container, objectFactory, redirectToSlash, defaultParentPackage);
    }
}
