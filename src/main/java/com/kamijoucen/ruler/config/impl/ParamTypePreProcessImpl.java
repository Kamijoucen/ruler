package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.ParamTypePreProcess;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParamTypePreProcessImpl implements ParamTypePreProcess {

    @Override
    public List<RulerParameter> process(Map<String, Object> param) {
        if (CollectionUtil.isEmpty(param)) {
            return Collections.emptyList();
        }
        List<RulerParameter> list = new ArrayList<RulerParameter>(param.size());
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            list.add(processOne(entry));
        }
        return list;
    }

    private RulerParameter processOne(Map.Entry<String, Object> entry) {

        Object value = entry.getValue();
        Class<?> valClass = value.getClass();

        // TODO

        return new RulerParameter(null, entry.getKey(), null);
    }
}
