package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        Path path = Paths.get((String) param[0]);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RulerRuntimeException(e.getMessage(), e);
            }
        }
        String content = (String) param[1];
        try {
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            throw new RulerRuntimeException(e.getMessage(), e);
        }
        return null;
    }

}
