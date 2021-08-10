package com.kamijoucen.ruler;

import com.kamijoucen.ruler.module.RulerCompiler;
import com.kamijoucen.ruler.module.RulerFile;
import com.kamijoucen.ruler.runtime.Scope;

public class Ruler {

    private static final Scope globalScope = new Scope("root", null);

    static {
        Init.engineInit(globalScope);
    }

    public static RuleScript compile(String text) {

        RulerFile file = RulerCompiler.compile(text);

        return new RuleScript(new Scope("file", globalScope), file);
    }
}
