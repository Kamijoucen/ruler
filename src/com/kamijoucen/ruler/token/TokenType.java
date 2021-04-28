package com.kamijoucen.ruler.token;

public enum TokenType {

    IDENTIFIER,
    OUT_IDENTIFIER,
    INTEGER,
    DOUBLE,
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

    EOF,

    UN_KNOW,
}
