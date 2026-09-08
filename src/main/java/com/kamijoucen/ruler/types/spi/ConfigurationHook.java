package com.kamijoucen.ruler.types.spi;

import com.kamijoucen.ruler.types.config.RulerConfiguration;

public interface ConfigurationHook {

    void hook(RulerConfiguration configuration);

}
