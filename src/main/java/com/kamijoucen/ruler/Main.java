package com.kamijoucen.ruler;

import java.io.File;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.config.impl.StartConfig;
import com.kamijoucen.ruler.module.ShellRunner;
import com.kamijoucen.ruler.util.IOUtil;

import java.io.IOException;

/**
 * Ruler解释器主入口
 * 支持交互式shell和脚本文件执行
 *
 * @author Kamijoucen
 */
public class Main {

    private static final RulerConfigurationImpl configuration = new RulerConfigurationImpl();
    static {
        // 默认初始化，需要从参数中覆盖
        configuration.setMaxLoopNumber(1000000);
        configuration.setMaxStackDepth(500);
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            // 无参数，启动交互式shell
            startShell();
            return;
        }

        String arg = args[0];

        if ("-v".equals(arg) || "--version".equals(arg)) {
            // 显示版本信息
            System.out.println("Ruler Script Language v1.0");
            return;
        }

        if ("-h".equals(arg) || "--help".equals(arg)) {
            // 显示帮助信息
            printHelp();
            return;
        }

        if (arg.startsWith("-")) {
            // 未知选项
            System.err.println("错误: 未知选项 '" + arg + "'");
            System.err.println("使用 -h 或 --help 查看帮助信息");
            System.exit(1);
            return;
        }

        // 执行脚本文件
        try {
            executeScriptFile(arg);
        } catch (IOException e) {
            System.err.println("错误: 无法读取文件 '" + arg + "'");
            System.err.println("原因: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 执行脚本文件
     *
     * @param filePath 脚本文件路径
     * @throws IOException 如果文件读取失败
     */
    private static void executeScriptFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        if (!file.isFile()) {
            throw new IOException("不是一个文件: " + filePath);
        }

        if (!file.canRead()) {
            throw new IOException("文件不可读: " + filePath);
        }

        String content = IOUtil.read(file);

        if (content == null || content.trim().isEmpty()) {
            System.out.println("警告: 文件为空");
            return;
        }

        // TODO: 编译并执行脚本
        System.out.println("执行脚本: " + filePath);
        // Ruler.compileScript(content, new RulerConfigurationImpl()).run();
    }

    /**
     * 打印帮助信息
     */
    private static void printHelp() {
        System.out.println("用法: ruler [选项] [脚本文件]");
        System.out.println();
        System.out.println("选项:");
        System.out.println("  -h, --help      显示此帮助信息");
        System.out.println("  -v, --version   显示版本信息");
        System.out.println();
        System.out.println("如果不提供脚本文件，将启动交互式shell");
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
