package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReturnAndMultiValueTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    @Test
    public void rootScriptMultiReturnTest() {
        RulerResult result = Ruler.compile("return 1, 2, 3;", configuration).run();
        Assert.assertEquals(3, result.size());
        Assert.assertEquals(1L, result.getResult().get(0).toInteger());
        Assert.assertEquals(2L, result.getResult().get(1).toInteger());
        Assert.assertEquals(3L, result.getResult().get(2).toInteger());
    }

    @Test
    public void emptyReturnProducesNoValuesTest() {
        Assert.assertEquals(0, Ruler.compile("return;", configuration).run().size());
    }

    @Test
    public void functionMultiReturnCanBeIndexedTest() {
        Assert.assertEquals(2L,
                Ruler.compile("fun pair() { return 1, 2; } return pair()[1];", configuration)
                        .run().first().toInteger());
    }

    @Test
    public void returnInsideIfStopsFollowingStatementsTest() {
        Assert.assertEquals(1L,
                Ruler.compile("if true { return 1; } return 2;", configuration)
                        .run().first().toInteger());
    }
}
