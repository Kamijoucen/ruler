package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.api.RulerRunner;
import com.kamijoucen.ruler.types.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Test;

public class EmptyProgramTest {

    @Test
    public void emptyScriptRunTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        RulerRunner runner = Ruler.compile("", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void emptyScriptWithWhitespaceTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        RulerRunner runner = Ruler.compile("   \n\t  ", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void emptyBlockTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        RulerRunner runner = Ruler.compile("var a = {}; return typeof(a);", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals("object", result.first().toString());
    }
}
