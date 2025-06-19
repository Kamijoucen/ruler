package com.kamijoucen.ruler;

import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;

import java.util.Objects;

/**
 * Ruler脚本引擎的主入口类
 * 提供编译脚本和表达式的静态方法
 *
 * @author Kamijoucen
 */
public class Ruler {

    /**
     * 私有构造函数，防止实例化
     */
    private Ruler() {
        throw new AssertionError("No instances of Ruler");
    }

    /**
     * 编译完整的脚本
     *
     * @param text 脚本源代码
     * @param configuration 配置对象
     * @return 可执行的RulerRunner对象
     * @throws NullPointerException 如果参数为null
     */
    public static RulerRunner compileScript(String text, RulerConfiguration configuration) {
        Objects.requireNonNull(text, "Script text cannot be null");
        Objects.requireNonNull(configuration, "Configuration cannot be null");

        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileScript();
        return new RulerRunner(module, true, configuration);
    }

    /**
     * 编译单个表达式
     *
     * @param text 表达式源代码
     * @param configuration 配置对象
     * @return 可执行的RulerRunner对象
     * @throws NullPointerException 如果参数为null
     */
    public static RulerRunner compileExpression(String text, RulerConfiguration configuration) {
        Objects.requireNonNull(text, "Expression text cannot be null");
        Objects.requireNonNull(configuration, "Configuration cannot be null");

        RulerScript script = new RulerScript();
        script.setContent(text);
        RulerModule module = new RulerCompiler(script, configuration).compileExpression();
        return new RulerRunner(module, false, configuration);
    }
}
