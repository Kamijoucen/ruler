package com.kamijoucen.ruler.types.runtime;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;

public final class CallDepth {

    private int currentStackDepth = 0;

    public void enter(int maxDepth) {
        if (maxDepth > 0 && currentStackDepth >= maxDepth) {
            throw new RulerRuntimeException("Stack depth exceeded! max: " + maxDepth);
        }
        ++currentStackDepth;
    }

    public void leave() {
        --currentStackDepth;
    }

    public int getDepth() {
        return currentStackDepth;
    }

}
