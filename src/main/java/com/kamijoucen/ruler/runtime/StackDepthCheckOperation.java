package com.kamijoucen.ruler.runtime;

public class StackDepthCheckOperation {

    private int currentStackDepth = 0;

    public void addDepth(RuntimeContext context) {
        ++currentStackDepth;
        if (context.getConfiguration().getMaxStackDepth() <= 0) {
            return;
        }
        if (context.getConfiguration().getMaxStackDepth() < currentStackDepth) {
            throw new RuntimeException("Stack depth exceeded! max: " + context.getConfiguration().getMaxStackDepth());
        }
    }

    public void subDepth(RuntimeContext context) {
        --currentStackDepth;
    }

    public int getCurrentStackDepth() {
        return currentStackDepth;
    }

}
