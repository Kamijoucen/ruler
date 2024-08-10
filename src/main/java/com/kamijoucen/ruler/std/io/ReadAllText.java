package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadAllText implements RulerFunction {

    @Override
    public String getName() {
        return "ReadAll";
    }

    @Override
    public Object call(RuntimeContext context, Environment env, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        if (!(param[0] instanceof String)) {
            return null;
        }
        String path = (String) param[0];
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
