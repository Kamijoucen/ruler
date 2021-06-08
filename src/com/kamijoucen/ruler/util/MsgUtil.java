package com.kamijoucen.ruler.util;

public class MsgUtil {

    public static String of(String msg, int line, int column) {
        return msg + ", 位置在{line:" + line + ", column:" + column + "}";
    }

}
