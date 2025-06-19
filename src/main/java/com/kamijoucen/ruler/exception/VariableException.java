package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 变量异常
 * 表示变量相关的错误，如未定义、重复定义等
 *
 * @author Kamijoucen
 */
public class VariableException extends RulerException {

    private static final long serialVersionUID = 1L;

    public VariableException(String message, TokenLocation location) {
        super(ErrorType.VARIABLE_ERROR, message, location);
    }

    public VariableException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.VARIABLE_ERROR, message, location, cause);
    }

    /**
     * 变量未定义
     */
    public static VariableException undefined(String name, TokenLocation location) {
        String message = String.format("变量 '%s' 未定义", name);
        return new VariableException(message, location);
    }

    /**
     * 变量已定义
     */
    public static VariableException alreadyDefined(String name, TokenLocation location) {
        String message = String.format("变量 '%s' 已定义", name);
        return new VariableException(message, location);
    }

    /**
     * 常量不能修改
     */
    public static VariableException cannotModifyConstant(String name, TokenLocation location) {
        String message = String.format("不能修改常量 '%s'", name);
        return new VariableException(message, location);
    }

    /**
     * 不能在该作用域定义变量
     */
    public static VariableException cannotDefineInScope(String name, TokenLocation location) {
        String message = String.format("不能在当前作用域定义变量 '%s'", name);
        return new VariableException(message, location);
    }
}
