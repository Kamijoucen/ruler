package com.kamijoucen.ruler.types.parser;

/** Token position and grammar context for one parse. */
public final class ParseState {
    public final TokenStream tokens;
    public boolean root = true;
    public boolean inLoop;

    public ParseState(TokenStream tokens) {
        this.tokens = tokens;
    }
}
