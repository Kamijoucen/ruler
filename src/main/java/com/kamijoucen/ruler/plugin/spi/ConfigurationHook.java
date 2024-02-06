package com.kamijoucen.ruler.plugin.spi;

import com.kamijoucen.ruler.config.RulerConfiguration;

public interface ConfigurationHook {

    void hook(RulerConfiguration configuration);

}
