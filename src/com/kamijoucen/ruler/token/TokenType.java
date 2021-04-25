package com.kamijoucen.ruler.token;

public enum TokenType {

    IDENTIFIER,
    NUMBER,
    STRING,
    DOT,            // .
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )
    LT,             // <
    GT,             // >
    LE,             // <=
    GE,             // >=
    ASSIGN,         // =
    EQ,             // ==
    NE,             // <> or !=
    ADD,            // +
    SUB,            // -
    MUL,            // *
    DIV,            // /
    KEY_RETURN,     // return
    KEY_DEF,        // def
    KEY_IF,         // if
    KEY_FOR,        // for
    KEY_BREAK,      // break
    KEY_CONTINUE,   // continue
    KEY_LIST,       // list
    KEY_MAP,        // map
    KEY_FALSE,          // false
    KEY_TRUE,           // true

    UN_KNOW,
}
