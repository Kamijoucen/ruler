package com.kamijoucen.ruler.stdlib.io;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.module.ConfigModule;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.spi.ConfigurationHook;

public class StdIoHook implements ConfigurationHook {

    private static final String STD_IO = "io";

    @Override
    public void hook(RulerConfiguration configuration) {
        List<RulerFunction> functions = new ArrayList<>();
        functions.add(new DeleteFile());
        functions.add(new WriteNewText());
        functions.add(new ReadAllText());
        functions.add(new PrintFileList());
        configuration.getModules()
                .register(ConfigModule.createFunctionModule(STD_IO, functions));
    }
}
