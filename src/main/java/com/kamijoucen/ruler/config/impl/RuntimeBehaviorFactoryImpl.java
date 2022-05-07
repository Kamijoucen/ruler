package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.RuntimeBehaviorFactory;
import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.StackDepthCheckOperation;

public class RuntimeBehaviorFactoryImpl implements RuntimeBehaviorFactory {

    @Override
    public LoopCountCheckOperation createLoopCountCheckOperation() {
        return new LoopCountCheckOperation();
    }

    @Override
    public StackDepthCheckOperation createStackDepthCheckOperation() {
        return new StackDepthCheckOperation();
    }

}
