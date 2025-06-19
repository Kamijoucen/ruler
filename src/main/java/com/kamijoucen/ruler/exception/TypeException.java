package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.ValueType;

/**
 * 类型异常
 * 表示类型不匹配或类型操作错误
 *
 * @author Kamijoucen
 */
public class TypeException extends RulerException {

    private static final long serialVersionUID = 1L;

    public TypeException(String message, TokenLocation location) {
        super(ErrorType.TYPE_ERROR, message, location);
    }

    public TypeException(String message, TokenLocation location, Throwable cause) {
        super(ErrorType.TYPE_ERROR, message, location, cause);
    }

    /**
     * 类型不匹配
     */
    public static TypeException typeMismatch(ValueType expected, ValueType actual, TokenLocation location) {
        String message = String.format("类型不匹配：期望 %s，实际为 %s", expected, actual);
        return new TypeException(message, location);
    }

    /**
     * 操作符不支持该类型
     */
    public static TypeException unsupportedOperation(String operator, ValueType type, TokenLocation location) {
        String message = String.format("类型 %s 不支持 '%s' 操作", type, operator);
        return new TypeException(message, location);
    }

    /**
     * 二元操作符类型不兼容
     */
    public static TypeException incompatibleTypes(String operator, ValueType left, ValueType right, TokenLocation location) {
        String message = String.format("操作符 '%s' 不能应用于类型 %s 和 %s", operator, left, right);
        return new TypeException(message, location);
    }

    /**
     * 不能转换类型
     */
    public static TypeException cannotConvert(ValueType from, ValueType to, TokenLocation location) {
        String message = String.format("无法将类型 %s 转换为 %s", from, to);
        return new TypeException(message, location);
    }

    /**
     * 不是可调用的类型
     */
    public static TypeException notCallable(ValueType type, TokenLocation location) {
        String message = String.format("类型 %s 不是可调用的", type);
        return new TypeException(message, location);
    }

    /**
     * 不支持索引操作
     */
    public static TypeException notIndexable(ValueType type, TokenLocation location) {
        String message = String.format("类型 %s 不支持索引操作", type);
        return new TypeException(message, location);
    }
}
