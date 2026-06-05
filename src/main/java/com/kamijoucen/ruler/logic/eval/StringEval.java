package com.kamijoucen.ruler.logic.eval.factor;

import com.kamijoucen.ruler.domain.ast.factor.StringNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.StringValue;

public class StringEval implements BaseEval<StringNode> {
    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return new StringValue(unescape(node.getValue()));
    }

    private String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\\' && i + 1 < s.length()) {
                char next = s.charAt(i + 1);
                if (next == '"' || next == '\\' || next == '{') {
                    sb.append(next);
                    i++;
                    continue;
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }
}
