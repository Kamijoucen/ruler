package com.kamijoucen.ruler;

import java.io.File;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.config.impl.StartConfig;
import com.kamijoucen.ruler.module.ShellRunner;
import com.kamijoucen.ruler.util.IOUtil;

public class Main {

    private static final RulerConfigurationImpl configuration = new RulerConfigurationImpl();
    static {
        // 默认初始化，需要从参数中覆盖
        configuration.setMaxLoopNumber(1000000);
        configuration.setMaxStackDepth(500);
    }

    public static void main(String[] args) {
        StartConfig config = parseConfig(args);
        // 应用配置
        handleConfig(config);
        if (IOUtil.isBlank(config.getFilePath())) {
            startShell();
        } else {
            startFile(config);
        }
    }

    private static void startShell() {
        ShellRunner shell = new ShellRunner(configuration);
        shell.run();
    }

    private static void startFile(StartConfig config) {
        String filePath = config.getFilePath();
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("\"" + filePath + "\" file not exists");
        }
        String content = IOUtil.read(file);
        if (IOUtil.isBlank(content)) {
            throw new IllegalArgumentException("\"" + filePath + "\" file is empty");

        }
        Ruler.compileScript(content, configuration).run();
    }

    private static StartConfig parseConfig(String[] args) {
        if (args.length == 0) {
            return new StartConfig();
        }
        StartConfig config = new StartConfig();
        for (String arg : args) {
            if (!arg.startsWith("-")) {
                throw new IllegalArgumentException("illegal argument: " + arg);
            }
            String[] split = arg.split("=");
            if (split.length != 2) {
                throw new IllegalArgumentException("illegal argument: " + arg);
            }
            String key = split[0];
            String value = split[1];
            switch (key) {
                case "-f":
                    config.setFilePath(value);
                    break;
                case "-maxLoopNumber":
                    config.setMaxLoopNumber(Integer.parseInt(value));
                    break;
                case "-maxStackDepth":
                    config.setMaxStackDepth(Integer.parseInt(value));
                    break;
                default:
                    throw new IllegalArgumentException("illegal argument: " + arg);
            }
        }
        return config;
    }

    private static void handleConfig(StartConfig config) {
        if (config.getMaxLoopNumber() > 0) {
            configuration.setMaxLoopNumber(config.getMaxLoopNumber());
        }
        if (config.getMaxStackDepth() > 0) {
            configuration.setMaxStackDepth(config.getMaxStackDepth());
        }
    }

}
