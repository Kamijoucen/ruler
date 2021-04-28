package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.BaseAST;

import java.util.List;

public interface Parser {

    List<BaseAST> parse();

}
