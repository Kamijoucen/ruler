package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.ImportNode;

public interface Parser {

    ImportNode parseImport();

    BaseNode parseStatement();

    BaseNode parseExpression();

}
