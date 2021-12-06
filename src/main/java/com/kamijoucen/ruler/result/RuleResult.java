package com.kamijoucen.ruler.result;

import com.kamijoucen.ruler.util.CollectionUtil;

import java.util.List;

public class RuleResult {

    private final List<RuleValue> result;

    public RuleResult(List<RuleValue> value) {
        this.result = value;
    }

    public List<RuleValue> getResult() {
        return result;
    }

    public boolean isEmpty() {
        return CollectionUtil.isEmpty(result);
    }

    public int size() {
        return result.size();
    }

    public RuleValue first() {
        if (CollectionUtil.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
