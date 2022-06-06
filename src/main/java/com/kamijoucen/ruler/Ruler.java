package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.module.RuleRunner;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.config.RulerConfiguration;

public class Ruler {

    public static RuleRunner compileScript(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileScript();
        return new RuleRunner(module, true, configuration);
    }

    public static RuleRunner compileExpression(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileExpression();
        return new RuleRunner(module, false, configuration);
    }
}
