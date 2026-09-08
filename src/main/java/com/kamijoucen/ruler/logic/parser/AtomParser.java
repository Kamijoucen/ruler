package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;

/**
 * Atomic parser contract owned by parser logic.
 */
public interface AtomParser {

    boolean support(TokenStream tokenStream);

    BaseNode parse(ParseState state);
}
