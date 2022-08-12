package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RuleRunner;
import org.junit.Before;
import org.junit.Test;

public class LoopTest {

    public RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
        configuration.setGlobalImportModule("/ruler/std/global.txt", "op");
    }


    @Test
    public void doubleBreakTest() {

        String script = "var i = 0; var r = while i < 10 { i = i + 1; while (true) { println('a'); break;} println(i);}; println(r);";

        RuleRunner runner = Ruler.compileScript(script, configuration);

        runner.run();
    }

}
