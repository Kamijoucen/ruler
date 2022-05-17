package com.kamijoucen.ruler.parameter;

import java.util.List;

public class SubRuleResultValue {

    private String name;
    private List<Object> values;

    public SubRuleResultValue(String name, List<Object> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
