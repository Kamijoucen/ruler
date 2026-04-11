package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import org.junit.Assert;
import org.junit.Test;

public class EmptyProgramTest {

    @Test
    public void emptyScriptRunTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript("", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void emptyScriptWithWhitespaceTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript("   \n\t  ", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals(0, result.size());
    }

    @Test(expected = SyntaxException.class)
    public void emptyExpressionCompileTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        Ruler.compileExpression("", configuration);
    }

    @Test
    public void emptyBlockTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript("var a = {}; return typeof(a);", configuration);
        RulerResult result = runner.run();
        Assert.assertEquals("object", result.first().toString());
    }
}
