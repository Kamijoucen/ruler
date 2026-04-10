package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteFile implements RulerFunction {

    @Override
    public String getName() {
        return "DeleteFile";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        if (!(param[0] instanceof String)) {
            return null;
        }
        try {
            Files.delete(Paths.get((String) param[0]));
        } catch (IOException e) {
            throw new RulerRuntimeException(e.getMessage(), e);
        }
        return null;
    }
}
