package com.kamijoucen.ruler.api;

import com.kamijoucen.ruler.logic.compiler.RulerCompiler;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.parameter.RulerResult;

public class Ruler {

    public static RulerRunner compile(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = RulerCompiler.compileScript(script, configuration);
        return new RulerRunner(module, configuration);
    }

    public static RulerResult run(String text, RulerConfiguration configuration) {
        return compile(text, configuration).run();
    }
}
