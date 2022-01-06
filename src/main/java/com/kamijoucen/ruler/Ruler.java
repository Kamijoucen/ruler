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

    public static RuleRunner compileScript(String text) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RuntimeConfig config = ConfigFactory.buildConfig(null);
        RulerProgram program = new RulerCompiler(config, script, globalScope).compileScript();
        return new RuleRunner(program, true);
    }

    public static RuleRunner compileExpression(String text) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerProgram program = new RulerCompiler(null, script, globalScope).compileExpression();
        return new RuleRunner(program, false);
    }
}
