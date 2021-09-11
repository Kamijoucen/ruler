package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.RulerCompiler;
import com.kamijoucen.ruler.config.ConfigFactory;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.runtime.Scope;

public class Ruler {

    static final Scope globalScope = new Scope("root", null);

    static {
        Init.engineInit(globalScope);
    }

    public static RuleRunner compile(String text) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RuntimeConfig config = ConfigFactory.buildConfig(null);
        RulerProgram program = new RulerCompiler(config, script, globalScope).compile();

        return new RuleRunner(program);
    }
}
