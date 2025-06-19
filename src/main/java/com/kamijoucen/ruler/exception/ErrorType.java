package com.kamijoucen.ruler.exception;

/**
 * 错误类型枚举
 * 定义了Ruler解释器中所有可能的错误类型
 *
 * @author Kamijoucen
 */
public enum ErrorType {

    // 语法错误
    SYNTAX_ERROR("语法错误"),

    // 词法错误
    LEXICAL_ERROR("词法错误"),

    // 运行时错误
    RUNTIME_ERROR("运行时错误"),

    // 类型错误
    TYPE_ERROR("类型错误"),

    // 变量错误
    VARIABLE_ERROR("变量错误"),

    // 函数调用错误
    FUNCTION_ERROR("函数错误"),

    // 操作符错误
    OPERATOR_ERROR("操作符错误"),

    // 导入错误
    IMPORT_ERROR("导入错误"),

    // 索引越界错误
    INDEX_ERROR("索引错误"),

    // 栈溢出错误
    STACK_OVERFLOW("栈溢出"),

    // 循环超限错误
    LOOP_LIMIT_ERROR("循环超限"),

    // IO错误
    IO_ERROR("IO错误"),

    // 参数错误
    ARGUMENT_ERROR("参数错误"),

    // 内部错误
    INTERNAL_ERROR("内部错误");

    private final String displayName;

    ErrorType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
