package com.kamijoucen.ruler.plugin.spi;

import com.kamijoucen.ruler.application.RulerConfiguration;

public interface ConfigurationHook {

    void hook(RulerConfiguration configuration);

}
