package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.plugin.spi.ConfigurationHook;

public class StdIoHook implements ConfigurationHook {

    private static final String STD_IO = "io";

    @Override
    public void hook(RulerConfiguration configuration) {
//        configuration.registerGlobalFunction(new DeleteFile(), STD_IO);
//        configuration.registerGlobalFunction(new WriteNewText(), STD_IO);
//        configuration.registerGlobalFunction(new ReadAllText(), STD_IO);
    }
}
