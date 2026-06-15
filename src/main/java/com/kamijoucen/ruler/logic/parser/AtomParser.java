package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;

/**
 * Atomic parser contract owned by parser logic.
 */
public interface AtomParser {

    boolean support(TokenStream tokenStream);

    BaseNode parse(ParserManager manager);
}
