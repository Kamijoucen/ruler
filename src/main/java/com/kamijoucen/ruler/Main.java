package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.RulerCompiler;
import com.kamijoucen.ruler.runtime.ConfigFactory;
import com.kamijoucen.ruler.runtime.RuntimeConfig;
import com.kamijoucen.ruler.compiler.RulerInterpreter;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.util.IOUtil;

import java.io.File;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        RuntimeConfig config = ConfigFactory.buildConfig(args);

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
        script.setFileName(file.getName());
        script.setContent(content);
        script.setPath(file.getParentFile().getAbsolutePath());

        RulerModule program = new RulerCompiler(config, script, Ruler.globalScope).compileScript();

        new RulerInterpreter(program).runScript(Collections.<String, Object>emptyMap());
    }

}
