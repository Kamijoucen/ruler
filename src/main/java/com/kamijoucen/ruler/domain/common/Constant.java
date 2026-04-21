package com.kamijoucen.ruler.domain.common;


public class Constant {

    public static final char ENTER = '\n';

    public static final char EOF = '\0';

    public static final String FUN_ARG_LIST = "_args_";

    public static final String RCLASS_PROPERTIES = "_properties_";

    public static final String RSON_FIELDS = "_fields_";

    public static final String PROXY_TARGET = "_target_";

    public static boolean isReservedName(String name) {
        return name != null && name.length() >= 3
                && name.charAt(0) == '_'
                && name.charAt(name.length() - 1) == '_';
    }

}
