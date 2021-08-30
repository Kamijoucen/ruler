package com.kamijoucen.ruler.common;

import java.util.ArrayList;
import java.util.List;

public class Constant {

    public static final List<String> SCRIPT_SUFFIX = new ArrayList<String>() {
        {
            this.add(".txt");
            this.add(".rule");
        }
    };

    public static final char EOF = '\0';

    public static final String CALL_VALUE = "temp_call_value";

    public static final String LIB_PATH_KEY = "RULER_LIB_KEY";

}
