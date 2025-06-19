package com.kamijoucen.ruler.std.io;

import com.kamijoucen.ruler.exception.RulerRuntimeException;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.StringValue;

/**
 * 读取文件全部内容的函数
 *
 * @author Kamijoucen
 */
public class ReadAllText implements RulerFunction {

    @Override
    public String getName() {
        return "ReadAll";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return "";
        }
        String fileName = param[0].toString();
        String content = IOUtil.read(fileName);
        if (content == null || content.isEmpty()) {
            // IOUtil.read 在文件不存在时返回空字符串
            throw new RulerRuntimeException("无法读取文件: " + fileName, null);
        }
        return new StringValue(content);
    }
}
