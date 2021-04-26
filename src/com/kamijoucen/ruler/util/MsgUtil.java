package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.token.TokenLocation;

public class MsgUtil {

    public static String of(String msg, int line, int column) {
        return msg + ", 位置在{line:" + line + ", column:" + column + "}";
    }

}
