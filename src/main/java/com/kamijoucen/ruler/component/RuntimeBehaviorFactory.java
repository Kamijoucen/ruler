package com.kamijoucen.ruler.component;

public interface RuntimeBehaviorFactory {

    LoopCountCheckOperation createLoopCountCheckOperation();

    StackDepthCheckOperation createStackDepthCheckOperation();

}
