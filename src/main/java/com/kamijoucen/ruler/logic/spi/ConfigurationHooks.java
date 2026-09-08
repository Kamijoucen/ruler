package com.kamijoucen.ruler.logic.spi;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.spi.ConfigurationHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public final class ConfigurationHooks {
    private ConfigurationHooks() {}

    private static final Logger log = LoggerFactory.getLogger(ConfigurationHooks.class);

    public static void load(RulerConfiguration configuration) {
        ServiceLoader<ConfigurationHook> loader = ServiceLoader.load(ConfigurationHook.class);
        for (ConfigurationHook hook : loader) {
            try {
                hook.hook(configuration);
            } catch (Exception e) {
                log.error("load spi error", e);
            }
        }
    }

}
