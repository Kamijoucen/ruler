package com.kamijoucen.ruler;

import java.io.File;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
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
        if (args == null || args.length == 0) {
            startShell();
        } else {
            startFile(args);
        }
    }

    private static void startShell() {
        ShellRunner shell = new ShellRunner(configuration);
        shell.run();
    }

    private static void startFile(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("args is empty");
        }

        File file = new File(args[0]);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("\"" + args[0] + "\" file not exists");
        }

        String content = IOUtil.read(file);
        if (IOUtil.isBlank(content)) {
            throw new IllegalArgumentException("\"" + args[0] + "\" file is empty");

        }
        Ruler.compileScript(content, configuration).run();
    }

}
