package lab.s2jh.ctx;

import java.io.File;

import lab.s2jh.sys.entity.ConfigProperty;
import lab.s2jh.sys.service.ConfigPropertyService;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 基于数据库加载动态配置参数
 * 框架扩展属性加载：Spring除了从.properties加载属性数据
 * 并且数据库如果存在同名属性则优先取数据库的属性值覆盖配置文件中的值
 * 为了避免意外的数据库配置导致系统崩溃，约定以cfg打头标识的参数表示可以被数据库参数覆写，其余的则不会覆盖文件定义的属性值
 */
@Component
public class DynamicConfigService {

    private final Logger logger = LoggerFactory.getLogger(DynamicConfigService.class);

    @Value("${cfg.signup.disabled:false}")
    private String signupDisabled;

    @Value("${cfg.system.title:S2JH}")
    private String systemTitle;

    @Value("${cfg.file.upload.dir:}")
    private String fileUploadDir;

    @Autowired(required = false)
    private ExtPropertyPlaceholderConfigurer extPropertyPlaceholderConfigurer;

    @Autowired
    private ConfigPropertyService configPropertyService;

    /**
     * 根据key获取对应动态参数值
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * 根据key获取对应动态参数值，如果没有则返回defaultValue
     */
    public String getString(String key, String defaultValue) {
        String val = null;
        //首先从数据库取值
        ConfigProperty cfg = configPropertyService.findByPropKey(key);
        if (cfg != null) {
            val = cfg.getSimpleValue();
        }
        //未取到则继续从Spring属性文件定义取

        if (val == null) {
            if (extPropertyPlaceholderConfigurer != null) {
                val = extPropertyPlaceholderConfigurer.getProperty(key);
            } else {
                logger.warn("当前不是以ExtPropertyPlaceholderConfigurer扩展模式定义，因此无法加载获取Spring属性配置");
            }
        }
        if (val == null) {
            return defaultValue;
        } else {
            return val.trim();
        }
    }

    public String getSystemTitle() {
        return getString("cfg.system.title", systemTitle);
    }

    public boolean isSignupDisabled() {
        return BooleanUtils.toBoolean(getString("cfg.signup.disabled", signupDisabled));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return BooleanUtils.toBoolean(getString(key, String.valueOf(defaultValue)));
    }

    private static String staticFileUploadDir;

    /**
     * 获取文件上传根目录：优先取cfg.file.upload.dir参数值，如果没有定义则取当前用户主目录${user.home}/attachments
     * @return
     */
    public String getFileUploadRootDir() {
        if (staticFileUploadDir == null) {
            staticFileUploadDir = fileUploadDir;
            if (StringUtils.isBlank(staticFileUploadDir)) {
                staticFileUploadDir = System.getProperty("user.home") + File.separator + "attachments";
            }
            if (staticFileUploadDir.endsWith(File.separator)) {
                staticFileUploadDir = staticFileUploadDir.substring(0, staticFileUploadDir.length() - 2);
            }
            logger.info("Setup file upload root dir:  {}", staticFileUploadDir);
        }
        return staticFileUploadDir;
    }
}
