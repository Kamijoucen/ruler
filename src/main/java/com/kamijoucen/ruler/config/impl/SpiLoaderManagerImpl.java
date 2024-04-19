package com.kamijoucen.ruler.config.impl;

import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.SpiLoaderManager;
import com.kamijoucen.ruler.plugin.spi.ConfigurationHook;

public class SpiLoaderManagerImpl implements SpiLoaderManager {

    private static final Logger log = LoggerFactory.getLogger(SpiLoaderManagerImpl.class);

    @Override
    public void load(RulerConfiguration configuration) {
        ServiceLoader<ConfigurationHook> loader = ServiceLoader.load(ConfigurationHook.class);
        for (ConfigurationHook hook : loader) {
            try {
                hook.hook(configuration);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("load spi error", e);
            }
        }
    }

}
