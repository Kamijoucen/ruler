package com.kamijoucen.ruler.std.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class PrintFileList implements RulerFunction {

    @Override
    public String getName() {
        return "PrintList";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return null;
        }
        if (!(param[0] instanceof String)) {
            return null;
        }
        Path path = Paths.get(param[0].toString());
        if (!Files.isDirectory(path)) {
            return null;
        }
        try {
            Files.list(path).map(Path::getFileName).forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
