package lab.s2jh.cfg;

import lab.s2jh.core.service.PropertiesConfigService;
import lab.s2jh.sys.entity.ConfigProperty;
import lab.s2jh.sys.service.ConfigPropertyService;

import org.apache.commons.lang3.BooleanUtils;
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
public class DynamicConfigService extends PropertiesConfigService {

    @Value("${cfg.signup.disabled:\"false\"}")
    private String signupDisabled;

    @Value("${cfg.system.title:\"S2JH\"}")
    private String systemTitle;

    @Value("${cfg.sms.mock.mode:\"false\"}")
    private String smsMockMode;

    @Autowired
    private ConfigPropertyService configPropertyService;

    private String getString(String key, String defaultValue) {
        ConfigProperty cfg = configPropertyService.findByPropKey(key);
        if (cfg == null) {
            return defaultValue;
        } else {
            return cfg.getSimpleValue();
        }
    }

    public String getSystemTitle() {
        return getString("cfg.system.title", systemTitle);
    }

    public boolean isSignupDisabled() {
        return BooleanUtils.toBoolean(getString("cfg.signup.disabled", signupDisabled));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        ConfigProperty cfg = configPropertyService.findByPropKey(key);
        if (cfg == null) {
            return defaultValue;
        } else {
            return BooleanUtils.toBoolean(cfg.getSimpleValue());
        }
    }
}
