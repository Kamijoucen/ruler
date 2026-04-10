package com.kamijoucen.ruler.component;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;

import com.kamijoucen.ruler.domain.ast.BaseNode;

public class LoopCountCheckOperation {

    private int count = 0;

    public void accept(BaseNode node, Scope scope, RuntimeContext context) {
        if (context.getConfiguration().getMaxLoopNumber() <= 0) {
            return;
        }
        if (context.getConfiguration().getMaxLoopNumber() < ++count) {
            throw new RuntimeException("Loop count exceeded! max: " + context.getConfiguration().getMaxLoopNumber()
                    + "\t" + node.getLocation().toString());
        }
    }
}
