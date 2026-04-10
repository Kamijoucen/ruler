package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.parameter.RulerParameter;

import java.util.List;
import java.util.Map;

public interface ParamTypePreProcess {

    List<RulerParameter> process(Map<String, Object> param);

}
