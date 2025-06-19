package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列出目录文件的函数
 *
 * @author Kamijoucen
 */
public class PrintFileList implements RulerFunction {

    @Override
    public String getName() {
        return "PrintFileList";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return new ArrayValue(new ArrayList<>());
        }

        String dirName = param[0].toString();
        Path path = Paths.get(dirName);

        try {
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                return new ArrayValue(new ArrayList<>());
            }

            List<BaseValue> fileList = Files.list(path)
                    .map(p -> new StringValue(p.getFileName().toString()))
                    .collect(Collectors.toList());

            return new ArrayValue(fileList);
        } catch (IOException e) {
            throw RulerRuntimeException.ioError("列出目录", dirName, null, e);
        }
    }
}
