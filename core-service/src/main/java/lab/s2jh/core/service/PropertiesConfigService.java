package lab.s2jh.core.service;

import lab.s2jh.core.annotation.MetaData;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfigService {

    private final Logger logger = LoggerFactory.getLogger(PropertiesConfigService.class);

    @MetaData(value = "开发模式", comments = "更宽松的权限控制，更多的日志信息。详见application.properties配置参数定义")
    private static boolean devMode = false;

    @MetaData(value = "演示模式", comments = "对演示环境进行特殊控制以避免不必要的随意数据修改导致系统混乱")
    private static boolean demoMode = false;

    public static boolean isDemoMode() {
        return demoMode;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    @Value("${demo.mode:false}")
    public void setDemoMode(String demoMode) {
        PropertiesConfigService.demoMode = BooleanUtils.toBoolean(demoMode);
        logger.info("System runnging at demo.mode={}", PropertiesConfigService.demoMode);
    }

    @Value("${dev.mode:false}")
    public void setDevMode(String devMode) {
        PropertiesConfigService.devMode = BooleanUtils.toBoolean(devMode);
        logger.info("System runnging at dev.mode={}", PropertiesConfigService.devMode);
    }
}
