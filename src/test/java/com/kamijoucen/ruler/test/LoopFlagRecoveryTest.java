package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 验证 ForEachParser/WhileParser 的 inLoop 标志在解析正常完成或异常时
 * 都能正确恢复到进入前的状态，避免污染后续顶层语句对 break/continue 的合法性检查。
 */
public class LoopFlagRecoveryTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    // ==================== 顶层 break/continue 应被拒绝 ====================

    @Test(expected = SyntaxException.class)
    public void topLevelBreakShouldBeRejected() {
        Ruler.compile("break;", configuration);
    }

    @Test(expected = SyntaxException.class)
    public void topLevelContinueShouldBeRejected() {
        Ruler.compile("continue;", configuration);
    }

    // ==================== 循环正常解析后，inLoop 应被恢复 ====================

    @Test(expected = SyntaxException.class)
    public void breakAfterWhileBlockShouldBeRejected() {
        // 第一段是 while 循环（合法），紧跟一个顶层 break（应非法）
        Ruler.compile("while false { break; } break;", configuration);
    }

    @Test(expected = SyntaxException.class)
    public void breakAfterForEachBlockShouldBeRejected() {
        Ruler.compile("for i in [1,2,3] { break; } break;", configuration);
    }

    @Test(expected = SyntaxException.class)
    public void continueAfterWhileBlockShouldBeRejected() {
        Ruler.compile("while false { continue; } continue;", configuration);
    }

    // ==================== 解析异常时 inLoop 也应被恢复 ====================

    /**
     * 第一段循环体故意写错（缺 `{` / `:`）触发 SyntaxException，
     * 但解析器内部应在 finally 块中恢复 inLoop=false，
     * 因此使用同一个 Configuration 重新编译一段含顶层 break 的脚本时仍应报错。
     */
    @Test
    public void inLoopFlagShouldBeRestoredAfterParseFailure() {
        // 1. 触发解析异常的脚本
        try {
            Ruler.compile("while true break;", configuration);
            Assert.fail("expected SyntaxException for malformed while loop");
        } catch (SyntaxException expected) {
            // ok
        }

        // 2. 第二次编译，顶层 break 应仍然被拒绝（证明 flag 已被恢复）
        try {
            Ruler.compile("break;", configuration);
            Assert.fail("expected SyntaxException for top-level break after recovery");
        } catch (SyntaxException expected) {
            // ok
        }
    }

    @Test
    public void inLoopFlagShouldBeRestoredAfterForEachParseFailure() {
        try {
            Ruler.compile("for i in [1,2,3] break;", configuration);
            Assert.fail("expected SyntaxException for malformed for loop");
        } catch (SyntaxException expected) {
            // ok
        }

        try {
            Ruler.compile("continue;", configuration);
            Assert.fail("expected SyntaxException for top-level continue after recovery");
        } catch (SyntaxException expected) {
            // ok
        }
    }

    // ==================== 嵌套循环：内层结束后外层仍处于循环中 ====================

    @Test
    public void nestedLoopsShouldKeepOuterFlag() {
        // 内层 while 退出后，外层 for 体内仍允许 break
        String script = "var n = 0;\n"
                + "for i in [1,2,3] {\n"
                + "    while n < 1 { n = n + 1; }\n"
                + "    if i == 2 { break; }\n"
                + "}\n"
                + "return n;";
        RulerRunner runner = Ruler.compile(script, configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(1L, result.first().toInteger());
    }

    @Test
    public void whileInsideForAllowsBreak() {
        String script = "var sum = 0;\n"
                + "for i in [1,2,3,4] {\n"
                + "    var k = 0;\n"
                + "    while k < 10 {\n"
                + "        if k == 2 { break; }\n"
                + "        k = k + 1;\n"
                + "    }\n"
                + "    sum = sum + k;\n"
                + "}\n"
                + "return sum;";
        Assert.assertEquals(8L, Ruler.compile(script, configuration).run().first().toInteger());
    }

    // ==================== 函数体内的循环不应让函数体外的 break 合法 ====================

    @Test(expected = SyntaxException.class)
    public void breakAfterFunctionContainingLoopShouldBeRejected() {
        Ruler.compile("fun f() { while false { break; } } break;", configuration);
    }
}
