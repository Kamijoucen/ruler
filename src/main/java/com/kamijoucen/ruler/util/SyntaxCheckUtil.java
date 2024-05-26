package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.compiler.impl.ParseContext;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyntaxCheckUtil {

    public static boolean isNumber(ValueType type) {
        return type == ValueType.INTEGER || type == ValueType.DOUBLE;
    }

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

    public static void availableImport(List<ImportNode> imports) {
        Set<String> aliasSet = new HashSet<>();
        for (ImportNode node : imports) {
            int oldAliasSize = aliasSet.size();
            aliasSet.add(node.getAlias());
            if (aliasSet.size() == oldAliasSize) {
                throw SyntaxException.withSyntax("别名重复:" + node.getAlias());
            }
        }
    }

}
