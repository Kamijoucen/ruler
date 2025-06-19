package com.kamijoucen.ruler.std.io;


import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 写入文本到文件的函数
 *
 * @author Kamijoucen
 */
public class WriteNewText implements RulerFunction {

    @Override
    public String getName() {
        return "WriteText";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length < 2) {
            return BoolValue.FALSE;
        }

        String fileName = param[0].toString();
        String text = param[1].toString();

        File file = new File(fileName);

        try {
            // 确保父目录存在
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    throw RulerRuntimeException.ioError("创建目录", parentDir.getPath(), null, null);
                }
            }

            // 写入文件
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(text);
            }

            return BoolValue.TRUE;
        } catch (IOException e) {
            throw RulerRuntimeException.ioError("写入文件", fileName, null, e);
        }
    }
}
