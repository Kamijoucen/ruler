package com.kamijoucen.ruler.config.impl;

import java.util.ServiceLoader;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.SpiLoaderManager;
import com.kamijoucen.ruler.plugin.spi.ConfigurationHook;

public class SpiLoaderManagerImpl implements SpiLoaderManager {

    @Override
    public void load(RulerConfiguration configuration) {
        ServiceLoader<ConfigurationHook> loader = ServiceLoader.load(ConfigurationHook.class);
        for (ConfigurationHook hook : loader) {
            hook.hook(configuration);
        }
    }


}
