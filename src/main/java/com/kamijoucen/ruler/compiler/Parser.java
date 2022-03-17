package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;

import java.util.List;

public interface Parser {

    List<BaseNode> parse();

}
