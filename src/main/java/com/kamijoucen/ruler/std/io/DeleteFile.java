package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 删除文件的函数
 *
 * @author Kamijoucen
 */
public class DeleteFile implements RulerFunction {

    @Override
    public String getName() {
        return "DeleteFile";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return BoolValue.FALSE;
        }

        String fileName = param[0].toString();
        Path path = Paths.get(fileName);

        try {
            if (Files.exists(path)) {
                Files.delete(path);
                return BoolValue.TRUE;
            } else {
                return BoolValue.FALSE;
            }
        } catch (IOException e) {
            throw RulerRuntimeException.ioError("删除文件", fileName, null, e);
        }
    }
}
