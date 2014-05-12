package lab.s2jh.core.web.convention;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.convention.DefaultResultMapBuilder;

import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.entities.ResultTypeConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;

public class ExtDefaultResultMapBuilder extends DefaultResultMapBuilder {

    @Inject
    public ExtDefaultResultMapBuilder(ServletContext servletContext, Container container,
            @Inject("struts.convention.relative.result.types") String relativeResultTypes) {
        super(servletContext, container, relativeResultTypes);
    }

    protected void makeResults(Class<?> actionClass, String path, String resultPrefix,
            Map<String, ResultConfig> results, PackageConfig packageConfig,
            Map<String, ResultTypeConfig> resultsByExtension) {

        //由于框架把jsp和js同前缀命名，导致convention插件把.js后缀文件作为搜索处理result type抛出异常
        //通过分析源码DefaultResultMapBuilder由于未提供相关配置参数，因此扩展特殊排除.js路径处理以抑制异常抛出
        if (path != null && path.endsWith(".js")) {
            return;
        }
        super.makeResults(actionClass, path, resultPrefix, results, packageConfig, resultsByExtension);
    }
}
