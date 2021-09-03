package com.kamijoucen.ruler.module;

import java.util.Map;

public class RulerProgram {

    private RulerModule mainModule;

    private Map<String, RulerModule> modules;

    public RulerModule getMainModule() {
        return mainModule;
    }

    public void setMainModule(RulerModule mainModule) {
        this.mainModule = mainModule;
    }

    public Map<String, RulerModule> getModules() {
        return modules;
    }

    public void setModules(Map<String, RulerModule> modules) {
        this.modules = modules;
    }

}
