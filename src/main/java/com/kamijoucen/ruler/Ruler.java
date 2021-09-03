package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.RulerCompiler;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.runtime.Scope;

public class Ruler {

    private static final Scope globalScope = new Scope("root", null);

    static {
        Init.engineInit(globalScope);
    }

    public static RuleRunner compile(String text) {

        RulerScript script = new RulerScript();
        script.setContent(text);

        RulerProgram program = new RulerCompiler().compile(script, null);

        return new RuleRunner(new Scope("file", globalScope), program);
    }
}
