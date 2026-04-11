package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Test;

public class EmptyProgramTest {

    @Test
    public void emptyScriptRunTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void emptyScriptWithWhitespaceTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("   \n\t  ", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void emptyBlockTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compile("var a = {}; return typeof(a);", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals("object", result.first().toString());
    }
}
