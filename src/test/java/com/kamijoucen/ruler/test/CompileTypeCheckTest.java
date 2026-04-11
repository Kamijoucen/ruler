package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompileTypeCheckTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    @Test
    public void stringSubtractionShouldFail() {
        try {
            Ruler.compile("'hello' - 'world'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '-' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void unaryNegationOnStringShouldFail() {
        try {
            Ruler.compile("-'abc'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unary operator '-' requires numeric type but got STRING"));
        }
    }

    @Test
    public void unaryAddOnStringShouldFail() {
        try {
            Ruler.compile("+'hello'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unary operator '+' requires numeric type but got STRING"));
        }
    }

    @Test
    public void logicalOpOnStringShouldFail() {
        try {
            Ruler.compile("'hello' && true", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("logical operator '&&' requires BOOL types but got STRING and BOOL"));
        }
    }

    @Test
    public void compareRsonAndArrayShouldFail() {
        try {
            Ruler.compile("{a:1} > [1,2]", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got RSON and ARRAY"));
        }
    }

    @Test
    public void stringCompareShouldFail() {
        try {
            Ruler.compile("'a' > 'b'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void booleanCompareGtShouldFail() {
        try {
            Ruler.compile("true > false", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got BOOL and BOOL"));
        }
    }

    @Test
    public void validNumericExpressionShouldCompile() {
        Assert.assertNotNull(Ruler.compile("1 + 2 * 3.0", configuration));
    }

    @Test
    public void stringAddWithPlusShouldFail() {
        try {
            Ruler.compile("'a' + 'b'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '+' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void mixedAddShouldFail() {
        try {
            Ruler.compile("1 + 'items'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '+' requires numeric types but got INT and STRING"));
        }
    }

    @Test
    public void validStringConcatShouldCompile() {
        Assert.assertNotNull(Ruler.compile("'hello' ++ 'world'", configuration));
    }

    @Test
    public void unknownVariableShouldNotFail() {
        Assert.assertNotNull(Ruler.compile("1 + unknownVar", configuration));
    }

    @Test
    public void variableTypePropagationShouldDetectError() {
        try {
            Ruler.compile("var a = 'hello'; var b = a - 1;", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '-' requires numeric types but got STRING and INT"));
        }
    }

    @Test
    public void variableTypePropagationShouldAllowValid() {
        Assert.assertNotNull(Ruler.compile("var a = 1; var b = a + 2;", configuration));
    }

    @Test
    public void ifConditionMustBeBool() {
        try {
            Ruler.compile("if 'notbool' { 1; }", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("condition of 'if' statement must be BOOL but got STRING"));
        }
    }

    @Test
    public void whileConditionMustBeBool() {
        try {
            Ruler.compile("while 'notbool' { break; }", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("condition of 'while' statement must be BOOL but got STRING"));
        }
    }

    @Test
    public void notOnNumberShouldFail() {
        try {
            Ruler.compile("!42", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '!' requires BOOL but got INT"));
        }
    }

    @Test
    public void equalityAlwaysAllowed() {
        Assert.assertNotNull(Ruler.compile("true == false", configuration));
        Assert.assertNotNull(Ruler.compile("'a' == 1", configuration));
    }

    @Test
    public void closureWithValidBodyShouldCompile() {
        Assert.assertNotNull(Ruler.compile("fun(x) { return x + 1; }", configuration));
    }

    @Test(expected = SyntaxException.class)
    public void closureWithInvalidBodyShouldFail() {
        Ruler.compile("fun(x) { x = 'hello'; return x - 1; }", configuration);
    }

    @Test
    public void forEachWithUnknownLoopVarShouldCompile() {
        Assert.assertNotNull(Ruler.compile("for i in [1,2,3] { println(i); }", configuration));
    }

}
