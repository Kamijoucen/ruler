package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.plugin.spi.ConfigurationHook;

public class StdIoHook implements ConfigurationHook {

    private static final String STD_IO = "io";

    @Override
    public void hook(RulerConfiguration configuration) {
        configuration.putGlobalFunction(new DeleteFile(), STD_IO);
        configuration.putGlobalFunction(new WriteNewText(), STD_IO);
        configuration.putGlobalFunction(new ReadAllText(), STD_IO);
    }
}
