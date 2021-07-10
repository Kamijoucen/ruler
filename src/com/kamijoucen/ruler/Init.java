package com.kamijoucen.ruler;

import com.kamijoucen.ruler.function.MakeItPossibleFunction;
import com.kamijoucen.ruler.function.PrintFunction;

class Init {

    static void engineInit() {
        Ruler.registerInnerFunction(new PrintFunction());
        Ruler.registerInnerFunction(new MakeItPossibleFunction());
    }

}
