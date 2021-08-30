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

    private RulerModule file;

    private Scope interScope;

    public RulerInterpreter(RulerModule file, Scope fileScope) {
        this.file = file;
        this.interScope = fileScope;
    }

    public List<Object> run(Map<String, Object> param, RuntimeConfig config) {

        Scope runScope = new Scope("run", interScope, ConvertUtil.convertToBase(param));


        runImports(file.getImportList());

        runScope.initReturnSpace();

        for (BaseNode ast : file.getStatements()) {
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

    private List<RulerModule> runImports(List<ImportNode> list) {

        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        for (ImportNode node : list) {

        }

        return null;
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
