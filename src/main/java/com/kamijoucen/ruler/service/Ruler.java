package com.kamijoucen.ruler.service;

import com.kamijoucen.ruler.component.RulerCompiler;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.module.RulerScript;

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
