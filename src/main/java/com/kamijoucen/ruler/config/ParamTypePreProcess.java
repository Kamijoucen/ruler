package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.parameter.RulerParameter;

import java.util.List;
import java.util.Map;

public interface ParamTypePreProcess {

    List<RulerParameter> process(Map<String, Object> param);

}
