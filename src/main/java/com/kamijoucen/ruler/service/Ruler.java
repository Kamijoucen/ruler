package com.kamijoucen.ruler.service;

import com.kamijoucen.ruler.component.RulerCompiler;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.module.RulerScript;
import com.kamijoucen.ruler.domain.parameter.RulerResult;

public class Ruler {

    public static RulerRunner compile(String text, RulerConfiguration configuration) {
        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileScript();
        return new RulerRunner(module, configuration);
    }

    public static RulerResult run(String text, RulerConfiguration configuration) {
        return compile(text, configuration).run();
    }
}
