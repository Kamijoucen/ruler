package com.kamijoucen.ruler.util;

import java.util.HashSet;
import java.util.Set;

import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.module.RulerModule;

public class SyntaxCheckUtil {

    public static void availableImport(RulerModule module) {

        AssertUtil.notNull(module);

        Set<String> pathSet = new HashSet<String>();
        Set<String> aliasSet = new HashSet<String>();

        for (ImportNode node : module.getImportList()) {

            int oldPathSize = pathSet.size();
            pathSet.add(node.getPath());
            if (pathSet.size() == oldPathSize) {
                throw SyntaxException.withSyntax("导入重复:" + node.getPath());
            }

            int oldAliasSize = aliasSet.size();
            aliasSet.add(node.getAlias());
            if (aliasSet.size() == oldAliasSize) {
                throw SyntaxException.withSyntax("别名重复:" + node.getAlias());
            }
        }
    }

}
