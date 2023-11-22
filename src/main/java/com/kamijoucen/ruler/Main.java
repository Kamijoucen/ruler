package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;

import java.io.File;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("空文件输入！");
        }

        File file = new File(args[0]);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("\"" + args[0] + "\"不是一个合法的文件");
        }

        String content = IOUtil.read(file);
        if (IOUtil.isBlank(content)) {
            throw new IllegalArgumentException("空文件输入！");

        }

        RulerScript script = new RulerScript();
        script.setFileName(args[0]);
        script.setContent(content);

        RulerConfiguration configuration = new RulerConfigurationImpl();
        RulerModule program = new RulerCompiler(script, configuration).compileScript();

        new RulerInterpreter(program, configuration)
                .runScript(Collections.<RulerParameter>emptyList(),
                        new Scope("main", true, configuration.getGlobalScope(), null));
    }

}
