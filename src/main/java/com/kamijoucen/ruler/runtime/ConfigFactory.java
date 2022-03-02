package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.Constant;

public class ConfigFactory {

    public static RuntimeConfig buildConfig(String[] args) {

        String libPath = System.getenv(Constant.LIB_PATH_KEY);

        RuntimeConfig config = new RuntimeConfig();
        config.libPath = libPath;

        return config;
    }

}
