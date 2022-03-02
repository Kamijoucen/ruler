package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.RulerCompiler;
import com.kamijoucen.ruler.runtime.ConfigFactory;
import com.kamijoucen.ruler.runtime.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerModule;
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
        RulerModule module = new RulerCompiler(config, script, globalScope).compileScript();
        return new RuleRunner(module, true);
    }

    public static RuleRunner compileExpression(String text) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(null, script, globalScope).compileExpression();
        return new RuleRunner(module, false);
    }
}
