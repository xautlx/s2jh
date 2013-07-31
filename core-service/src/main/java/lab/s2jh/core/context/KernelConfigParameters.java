package lab.s2jh.core.context;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KernelConfigParameters {

    @Value("${dev.mode}")
    private String devMode;

    public boolean isDevMode() {
        return BooleanUtils.toBoolean(devMode);
    }

}
