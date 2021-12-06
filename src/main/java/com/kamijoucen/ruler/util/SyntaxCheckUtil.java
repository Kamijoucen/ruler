package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.exception.SyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyntaxCheckUtil {

    public static void availableImport(List<ImportNode> imports) {

        Set<String> pathSet = new HashSet<String>();
        Set<String> aliasSet = new HashSet<String>();

        for (ImportNode node : imports) {
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
