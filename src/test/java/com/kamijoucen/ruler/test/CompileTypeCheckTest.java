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
            Ruler.compileExpression("'hello' - 'world'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '-' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void unaryNegationOnStringShouldFail() {
        try {
            Ruler.compileExpression("-'abc'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unary operator '-' requires numeric type but got STRING"));
        }
    }

    @Test
    public void unaryAddOnStringShouldFail() {
        try {
            Ruler.compileExpression("+'hello'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("unary operator '+' requires numeric type but got STRING"));
        }
    }

    @Test
    public void logicalOpOnStringShouldFail() {
        try {
            Ruler.compileExpression("'hello' && true", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("logical operator '&&' requires BOOL types but got STRING and BOOL"));
        }
    }

    @Test
    public void compareRsonAndArrayShouldFail() {
        try {
            Ruler.compileExpression("{a:1} > [1,2]", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got RSON and ARRAY"));
        }
    }

    @Test
    public void stringCompareShouldFail() {
        try {
            Ruler.compileExpression("'a' > 'b'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void booleanCompareGtShouldFail() {
        try {
            Ruler.compileExpression("true > false", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("comparison operator '>' requires numeric types but got BOOL and BOOL"));
        }
    }

    @Test
    public void validNumericExpressionShouldCompile() {
        Assert.assertNotNull(Ruler.compileExpression("1 + 2 * 3.0", configuration));
    }

    @Test
    public void stringAddWithPlusShouldFail() {
        try {
            Ruler.compileExpression("'a' + 'b'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '+' requires numeric types but got STRING and STRING"));
        }
    }

    @Test
    public void mixedAddShouldFail() {
        try {
            Ruler.compileExpression("1 + 'items'", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '+' requires numeric types but got INT and STRING"));
        }
    }

    @Test
    public void validStringConcatShouldCompile() {
        Assert.assertNotNull(Ruler.compileExpression("'hello' ++ 'world'", configuration));
    }

    @Test
    public void unknownVariableShouldNotFail() {
        Assert.assertNotNull(Ruler.compileExpression("1 + unknownVar", configuration));
    }

    @Test
    public void variableTypePropagationShouldDetectError() {
        try {
            Ruler.compileScript("var a = 'hello'; var b = a - 1;", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '-' requires numeric types but got STRING and INT"));
        }
    }

    @Test
    public void variableTypePropagationShouldAllowValid() {
        Assert.assertNotNull(Ruler.compileScript("var a = 1; var b = a + 2;", configuration));
    }

    @Test
    public void ifConditionMustBeBool() {
        try {
            Ruler.compileScript("if 'notbool' { 1; }", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("condition of 'if' statement must be BOOL but got STRING"));
        }
    }

    @Test
    public void whileConditionMustBeBool() {
        try {
            Ruler.compileScript("while 'notbool' { break; }", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("condition of 'while' statement must be BOOL but got STRING"));
        }
    }

    @Test
    public void notOnNumberShouldFail() {
        try {
            Ruler.compileExpression("!42", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("operator '!' requires BOOL but got INT"));
        }
    }

    @Test
    public void equalityAlwaysAllowed() {
        Assert.assertNotNull(Ruler.compileExpression("true == false", configuration));
        Assert.assertNotNull(Ruler.compileExpression("'a' == 1", configuration));
    }

    @Test
    public void closureWithValidBodyShouldCompile() {
        Assert.assertNotNull(Ruler.compileExpression("fun(x) { return x + 1; }", configuration));
    }

    @Test(expected = SyntaxException.class)
    public void closureWithInvalidBodyShouldFail() {
        Ruler.compileExpression("fun(x) { x = 'hello'; return x - 1; }", configuration);
    }

    @Test
    public void forEachWithUnknownLoopVarShouldCompile() {
        Assert.assertNotNull(Ruler.compileScript("for i in [1,2,3] { println(i); }", configuration));
    }

}
