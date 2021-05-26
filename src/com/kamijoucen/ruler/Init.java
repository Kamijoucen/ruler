package com.kamijoucen.ruler;

import com.kamijoucen.ruler.runtime.function.MakeItPossibleFunction;
import com.kamijoucen.ruler.runtime.function.PrintFunction;

class Init {

    static void engineInit() {
        Ruler.registerInnerFunction(new PrintFunction());
        Ruler.registerInnerFunction(new MakeItPossibleFunction());
    }

}
