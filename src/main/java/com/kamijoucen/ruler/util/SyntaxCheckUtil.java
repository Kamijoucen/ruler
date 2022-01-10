package com.kamijoucen.ruler.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.LogicBinaryOperationNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.parse.impl.ParseContext;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class SyntaxCheckUtil {

    public static void importPathCheck(String[] pathParts) {
        if (pathParts == null || pathParts.length == 0) {
            throw SyntaxException.withSyntax("The import path is empty");
        }
        for (String part : pathParts) {
            if (IOUtil.isBlank(part)) {
                throw SyntaxException.withSyntax("Illegal import matches");
            }
            for (char ch : part.toCharArray()) {
                if (!IOUtil.isAvailablePathChar(ch)) {
                    throw SyntaxException.withSyntax("Illegal character");
                }
            }
        }
    }

    public static void binaryTypeCheck(BinaryOperationNode node, ParseContext parseContext, RuntimeContext context) {
        BaseValue typeVal = parseContext.getTypeCheckVisitor().eval(node, null, context);
        if (typeVal.getType() == ValueType.FAILURE) {
            throw SyntaxException.withSyntax("表达式类型错误：" + node.toString());
        }
    }

    public static void logicBinaryTypeCheck(LogicBinaryOperationNode node, ParseContext parseContext,
            RuntimeContext context) {
        BaseValue typeVal = parseContext.getTypeCheckVisitor().eval(node, null, context);
        if (typeVal.getType() == ValueType.FAILURE) {
            throw SyntaxException.withSyntax("表达式类型错误：" + node.toString());
        }
    }

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
