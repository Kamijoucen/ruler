package com.kamijoucen.ruler.parameter;

import com.kamijoucen.ruler.util.CollectionUtil;

import java.util.List;

public class RuleResult {

    private final List<RuleResultValue> result;

    public RuleResult(List<RuleResultValue> value) {
        this.result = value;
    }

    public List<RuleResultValue> getResult() {
        return result;
    }

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(result);
    }

    public int size() {
        return result.size();
    }

    public RuleResultValue first() {
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
