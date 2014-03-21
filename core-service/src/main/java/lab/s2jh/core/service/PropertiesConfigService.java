package lab.s2jh.core.service;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfigService {

    @Value("${dev.mode:\"false\"}")
    private String devMode;

    public boolean isDevMode() {
        return BooleanUtils.toBoolean(devMode);
    }
}
