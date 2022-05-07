package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.runtime.LoopCountCheckOperation;
import com.kamijoucen.ruler.runtime.StackDepthCheckOperation;

public interface RuntimeBehaviorFactory {

    LoopCountCheckOperation createLoopCountCheckOperation();

    StackDepthCheckOperation createStackDepthCheckOperation();

}
