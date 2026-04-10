package com.kamijoucen.ruler.std.io;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.ConfigModule;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.plugin.spi.ConfigurationHook;

public class StdIoHook implements ConfigurationHook {

    private static final String STD_IO = "io";

    @Override
    public void hook(RulerConfiguration configuration) {
        List<RulerFunction> functions = new ArrayList<>();
        functions.add(new DeleteFile());
        functions.add(new WriteNewText());
        functions.add(new ReadAllText());
        functions.add(new PrintFileList());
        configuration.getConfigModuleManager()
                .registerModule(ConfigModule.createFunctionModule(STD_IO, functions));
    }
}
