package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.module.RulerFile;

import java.util.List;

public interface Parser {

    List<BaseNode> parse();

}
