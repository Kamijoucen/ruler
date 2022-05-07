package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.ast.BaseNode;

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
