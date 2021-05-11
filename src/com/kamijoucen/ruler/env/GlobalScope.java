package com.kamijoucen.ruler.env;

import com.kamijoucen.ruler.value.BaseValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalScope {

    private Map<String, BaseValue> GLOBAL_VALUES = new ConcurrentHashMap<String, BaseValue>();

}
