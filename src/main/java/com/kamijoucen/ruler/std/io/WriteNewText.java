package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteNewText implements RulerFunction {

    @Override
    public String getName() {
        return "WriteText";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            return null;
        }
        if (!(param[0] instanceof String) || !(param[1] instanceof String)) {
            return null;
        }
        String path = (String) param[0];
        String content = (String) param[1];
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
