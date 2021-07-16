package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface BaseNode {

    BaseValue eval(RuntimeContext context);

}
