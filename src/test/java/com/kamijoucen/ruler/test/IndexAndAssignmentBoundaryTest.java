package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexAndAssignmentBoundaryTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    @Test(expected = RulerRuntimeException.class)
    public void arrayNegativeIndexThrowsTest() {
        Ruler.compile("return [1, 2][-1];", configuration).run();
    }

    @Test(expected = RulerRuntimeException.class)
    public void stringNegativeIndexThrowsTest() {
        Ruler.compile("return 'ab'[-1];", configuration).run();
    }

    @Test
    public void missingObjectPropertyReturnsNullTest() {
        Assert.assertTrue(Ruler.compile("return ({})['missing'] === null;", configuration)
                .run().first().toBoolean());
    }

    @Test
    public void missingDotPropertyReturnsNullTest() {
        Assert.assertTrue(Ruler.compile("return ({a: 1}).missing === null;", configuration)
                .run().first().toBoolean());
    }

    @Test
    public void objectIndexAssignmentRequiresStringKeyTest() {
        try {
            Ruler.compile("var obj = {a: 1}; obj[1] = 2;", configuration).run();
            Assert.fail("Expected RulerRuntimeException");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("invalid key type for object"));
        }
    }

    @Test(expected = RulerRuntimeException.class)
    public void dotAssignmentOnNonObjectThrowsTest() {
        Ruler.compile("var value = 1; value.a = 2;", configuration).run();
    }
}
