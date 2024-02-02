package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;

public class Ruler {

    public static RulerRunner compileScript(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileScript();
        return new RulerRunner(module, true, configuration);
    }

    public static RulerRunner compileExpression(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileExpression();
        return new RulerRunner(module, false, configuration);
    }
}
