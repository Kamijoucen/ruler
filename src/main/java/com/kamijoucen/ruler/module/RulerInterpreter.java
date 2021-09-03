package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.common.ConvertRepository;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.ConvertUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RulerInterpreter {

    private RulerProgram program;

    private Scope interScope;

    public RulerInterpreter(RulerProgram program, Scope fileScope) {
        this.program = program;
        this.interScope = fileScope;
    }

    public List<Object> run(Map<String, Object> param, RuntimeConfig config) {

        Scope runScope = new Scope("run", interScope, ConvertUtil.convertToBase(param));

        // runImports(file.getImportList());

        runScope.initReturnSpace();

        for (BaseNode ast : program.getMainModule().getStatements()) {
            ast.eval(runScope);
        }

        List<BaseValue> returnValue = runScope.getReturnSpace();

        if (CollectionUtil.isEmpty(returnValue)) {
            return Collections.emptyList();
        }
        List<Object> realValue = new ArrayList<Object>(returnValue.size());

        for (BaseValue baseValue : returnValue) {
            realValue.add(ConvertRepository.getConverter(baseValue.getType()));
        }
        return realValue;
    }

    private List<RulerModule> loadLib(RuntimeConfig config) {

        AssertUtil.notNull(config);

        File libPath = new File(config.libPath);
        if (!libPath.exists() || !libPath.isDirectory()) {
            return Collections.emptyList();
        }



        return null;
    }

}
