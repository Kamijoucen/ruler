package com.kamijoucen.ruler.token;

public enum TokenType {

    IDENTIFIER,
    OUT_IDENTIFIER,
    INTEGER,
    DOUBLE,
    STRING,
    COMMA,          // ,
    SEMICOLON,      // ;
    COLON,          // :
    DOT,            // .
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )
    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }
    LEFT_SQUARE,    // [
    RIGHT_SQUARE,   // ]
    LT,             // <
    GT,             // >
    LE,             // <=
    GE,             // >=
    ASSIGN,         // =
    EQ,             // ==
    NE,             // <> or !=
    AND,            // &&
    OR,             // ||
    NOT,            // !
    ADD,            // +
    SUB,            // -
    MUL,            // *
    DIV,            // /
    CALL,           // ()
    INDEX,          // []
    KEY_RETURN,     // return
    KEY_DEF,        // def
    KEY_IF,         // if
    KEY_FOR,        // for
    KEY_WHILE,      // while
    KEY_BREAK,      // break
    KEY_CONTINUE,   // continue
    KEY_LIST,       // list
    KEY_MAP,        // map
    KEY_FALSE,      // false
    KEY_TRUE,       // true
    KEY_ELSE,       // else
    KEY_FUN,        // fun
    KEY_VAR,        // var
    KEY_NULL,       // null
    KEY_THIS,       // this

    EOF,

    UN_KNOW,
}
