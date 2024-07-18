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
    STRICT_EQ,      // ===
    STRICT_NE,      // !==
    HASH,           // #
    ARROW,          // ->
    NOT,            // !
    ADD,            // +
    STRING_ADD,     // ++
    SUB,            // -
    MUL,            // *
    DIV,            // /
    CALL,           // ()
    INDEX,          // []
    KEY_RETURN,     // return
    KEY_IF,         // if
    KEY_FOR,        // for
    KEY_WHILE,      // while
    KEY_BREAK,      // break
    KEY_CONTINUE,   // continue
    KEY_FALSE,      // false
    KEY_TRUE,       // true
    KEY_ELSE,       // else
    KEY_FUN,        // fun
    KEY_VAR,        // var
    KEY_CONST,      // const
    KEY_NULL,       // null
    KEY_THIS,       // this
    KEY_IMPORT,     // import
    KEY_INIT,       // init
    KEY_TYPEOF,     // typeof
    KEY_INFIX,      // infix
    KEY_IN,         // in
    KEY_RULE,       // rule

    KEY_ENTER,      // enter

    KEY_MATCH,      // match

    EOF,

    UN_KNOW,
}
