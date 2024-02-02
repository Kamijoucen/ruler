package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;

public interface Parser {

    ImportNode parseImport();

    BaseNode parseStatement();

    BaseNode parseExpression();

}
