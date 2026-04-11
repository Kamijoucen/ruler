package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.component.ParseContext;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.type.RulerType;
import com.kamijoucen.ruler.domain.type.TypeKind;
import com.kamijoucen.ruler.domain.value.ValueType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyntaxCheckUtil {

    public static boolean isNumber(ValueType type) {
        return type == ValueType.INTEGER || type == ValueType.DOUBLE;
    }

    public static void importPathCheck(String[] pathParts) {
        if (pathParts == null || pathParts.length == 0) {
            throw new SyntaxException("import path is empty");
        }
        for (String part : pathParts) {
            if (IOUtil.isBlank(part)) {
                throw new SyntaxException("invalid import path segment");
            }
            for (char ch : part.toCharArray()) {
                if (!IOUtil.isAvailablePathChar(ch)) {
                    throw new SyntaxException("invalid character in import path");
                }
            }
        }
    }

    public static void binaryTypeCheck(BinaryOperationNode node, ParseContext parseContext, RuntimeContext context) {
        RulerType typeVal = parseContext.getTypeCheckVisitor().eval(node, null, context);
        if (typeVal.getKind() == TypeKind.FAILURE) {
            throw new SyntaxException("type error: " + node);
        }
    }

    public static void availableImport(List<ImportNode> imports) {
        Set<String> aliasSet = new HashSet<>();
        for (ImportNode node : imports) {
            int oldAliasSize = aliasSet.size();
            aliasSet.add(node.getAlias());
            if (aliasSet.size() == oldAliasSize) {
                throw new SyntaxException("duplicate alias: " + node.getAlias());
            }
        }
    }

}
