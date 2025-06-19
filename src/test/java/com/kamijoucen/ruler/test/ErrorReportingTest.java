package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.exception.RulerException;
import com.kamijoucen.ruler.module.RulerRunner;

/**
 * 错误报告测试
 * 展示新的错误处理系统的效果
 *
 * @author Kamijoucen
 */
public class ErrorReportingTest {

    public static void main(String[] args) {
        testSyntaxError();
        testVariableError();
        testTypeError();
        testRuntimeError();
    }

        private static void testSyntaxError() {
        System.out.println("=== 测试语法错误 ===");
        String script =
            "var x = 10;\n" +
            "if x > 5 \n" +
            "    println(\"x is greater than 5\");\n" +
            "}\n";

        try {
            RulerConfigurationImpl config = new RulerConfigurationImpl();
            RulerRunner runner = Ruler.compileScript(script, config);
            runner.run();
        } catch (RulerException e) {
            System.out.println(e.getFormattedMessage());
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testVariableError() {
        System.out.println("=== 测试变量错误 ===");
        String script =
            "var x = 10;\n" +
            "y = x + 5;\n" +
            "println(y);\n";

        try {
            RulerConfigurationImpl config = new RulerConfigurationImpl();
            RulerRunner runner = Ruler.compileScript(script, config);
            runner.run();
        } catch (RulerException e) {
            System.out.println(e.getFormattedMessage());
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testTypeError() {
        System.out.println("=== 测试类型错误 ===");
        String script =
            "var x = \"hello\";\n" +
            "var y = 10;\n" +
            "var z = x + y;\n" +
            "println(z);\n";

        try {
            RulerConfigurationImpl config = new RulerConfigurationImpl();
            RulerRunner runner = Ruler.compileScript(script, config);
            runner.run();
        } catch (RulerException e) {
            System.out.println(e.getFormattedMessage());
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    private static void testRuntimeError() {
        System.out.println("=== 测试运行时错误 ===");
        String script =
            "var arr = [1, 2, 3];\n" +
            "var x = arr[10];\n" +
            "println(x);\n";

        try {
            RulerConfigurationImpl config = new RulerConfigurationImpl();
            RulerRunner runner = Ruler.compileScript(script, config);
            runner.run();
        } catch (RulerException e) {
            System.out.println(e.getFormattedMessage());
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }
}
