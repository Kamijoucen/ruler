package com.kamijoucen.ruler.component;

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
