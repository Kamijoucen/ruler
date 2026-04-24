package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.parameter.RulerResult;
import com.kamijoucen.ruler.service.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class StdLibraryTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerResult run(String text) {
        return Ruler.compile(text, configuration).run();
    }

    @SuppressWarnings("unchecked")
    private List<Object> list(RulerResult r) {
        return (List<Object>) r.first().getValue();
    }

    // ==================== math ====================

    @Test
    public void mathAbsTest() {
        Assert.assertEquals(5L, run("return abs(-5);").first().toInteger());
        Assert.assertEquals(5.5, run("return abs(-5.5);").first().toDouble(), 0.0001);
    }

    @Test
    public void mathMinTest() {
        Assert.assertEquals(1L, run("return min(1, 5);").first().toInteger());
        Assert.assertEquals(1.5, run("return min(1.5, 5);").first().toDouble(), 0.0001);
    }

    @Test
    public void mathMaxTest() {
        Assert.assertEquals(5L, run("return max(1, 5);").first().toInteger());
        Assert.assertEquals(5.0, run("return max(1.5, 5);").first().toDouble(), 0.0001);
    }

    @Test
    public void mathRoundTest() {
        Assert.assertEquals(3L, run("return round(2.6);").first().toInteger());
        Assert.assertEquals(2L, run("return round(2.4);").first().toInteger());
    }

    @Test
    public void mathFloorTest() {
        Assert.assertEquals(2L, run("return floor(2.9);").first().toInteger());
    }

    @Test
    public void mathCeilTest() {
        Assert.assertEquals(3L, run("return ceil(2.1);").first().toInteger());
    }

    @Test
    public void mathPowTest() {
        Assert.assertEquals(8.0, run("return pow(2, 3);").first().toDouble(), 0.0001);
    }

    @Test
    public void mathSqrtTest() {
        Assert.assertEquals(3.0, run("return sqrt(9);").first().toDouble(), 0.0001);
    }

    @Test
    public void mathRandomTest() {
        double v = run("return random();").first().toDouble();
        Assert.assertTrue(v >= 0.0 && v < 1.0);
    }

    // ==================== string native functions ====================

    @Test
    public void stringSubstringTest() {
        Assert.assertEquals("ell", run("return stringSubstring('hello', 1, 4);").first().toString());
        Assert.assertEquals("llo", run("return stringSubstring('hello', 2);").first().toString());
    }

    @Test
    public void stringIndexOfTest() {
        Assert.assertEquals(1L, run("return stringIndexOf('hello', 'el');").first().toInteger());
        Assert.assertEquals(-1L, run("return stringIndexOf('hello', 'z');").first().toInteger());
    }

    @Test
    public void stringReplaceTest() {
        Assert.assertEquals("heXXo", run("return stringReplace('hello', 'll', 'XX');").first().toString());
    }

    @Test
    public void stringSplitTest() {
        RulerResult r = run("return stringSplit('a,b,c', ',');");
        List<?> list = list(r);
        Assert.assertEquals(3, list.size());
    }

    @Test
    public void stringTrimTest() {
        Assert.assertEquals("abc", run("return stringTrim('  abc  ');").first().toString());
    }

    @Test
    public void stringCaseTest() {
        Assert.assertEquals("HELLO", run("return stringUpperCase('hello');").first().toString());
        Assert.assertEquals("hello", run("return stringLowerCase('HELLO');").first().toString());
    }

    @Test
    public void stringStartsEndsWithTest() {
        Assert.assertTrue(run("return stringStartsWith('hello', 'he');").first().toBoolean());
        Assert.assertTrue(run("return stringEndsWith('hello', 'lo');").first().toBoolean());
    }

    // ==================== string object methods ====================

    @Test
    public void stringObjectMethodsTest() {
        Assert.assertEquals(5L, run("return 'hello'.length();").first().toInteger());
        Assert.assertEquals("ell", run("return 'hello'.substring(1, 4);").first().toString());
        Assert.assertEquals(1L, run("return 'hello'.indexOf('el');").first().toInteger());
        Assert.assertEquals("heXXo", run("return 'hello'.replace('ll', 'XX');").first().toString());
        Assert.assertEquals(3L, run("return 'a,b,c'.split(',').length();").first().toInteger());
        Assert.assertEquals("abc", run("return '  abc  '.trim();").first().toString());
        Assert.assertEquals("HELLO", run("return 'hello'.upperCase();").first().toString());
        Assert.assertEquals("hello", run("return 'HELLO'.lowerCase();").first().toString());
        Assert.assertTrue(run("return 'hello'.startsWith('he');").first().toBoolean());
        Assert.assertTrue(run("return 'hello'.endsWith('lo');").first().toBoolean());
        Assert.assertEquals("e", run("return 'hello'.charAt(1);").first().toString());
    }

    // ==================== array native functions ====================

    @Test
    public void arrayMapTest() {
        RulerResult r = run("return arrayMap([1, 2, 3], fun(x) { return x * 2; });");
        List<?> list = list(r);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(2), list.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(6), list.get(2));
    }

    @Test
    public void arrayFilterTest() {
        RulerResult r = run("return arrayFilter([1, 2, 3, 4], fun(x) { return x > 2; });");
        List<?> list = list(r);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(3), list.get(0));
    }

    @Test
    public void arrayReduceTest() {
        Assert.assertEquals(10L, run("return arrayReduce([1, 2, 3, 4], fun(a, b) { return a + b; }, 0);").first().toInteger());
        Assert.assertEquals(24L, run("return arrayReduce([2, 3, 4], fun(a, b) { return a * b; });").first().toInteger());
    }

    @Test
    public void arrayFindTest() {
        Assert.assertEquals(3L, run("return arrayFind([1, 2, 3, 4], fun(x) { return x > 2; });").first().toInteger());
        Assert.assertNull(run("return arrayFind([1, 2], fun(x) { return x > 5; });").first().getValue());
    }

    @Test
    public void arrayFindIndexTest() {
        Assert.assertEquals(2L, run("return arrayFindIndex([1, 2, 3, 4], fun(x) { return x > 2; });").first().toInteger());
        Assert.assertEquals(-1L, run("return arrayFindIndex([1, 2], fun(x) { return x > 5; });").first().toInteger());
    }

    @Test
    public void arraySliceTest() {
        RulerResult r = run("return arraySlice([1, 2, 3, 4], 1, 3);");
        List<?> list = list(r);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(2), list.get(0));
    }

    @Test
    public void arrayConcatTest() {
        RulerResult r = run("return arrayConcat([1, 2], [3, 4]);");
        Assert.assertEquals(4, list(r).size());
    }

    @Test
    public void arrayJoinTest() {
        Assert.assertEquals("a-b", run("return arrayJoin(['a', 'b'], '-');").first().toString());
    }

    @Test
    public void arrayReverseTest() {
        RulerResult r = run("var a = [1, 2, 3]; arrayReverse(a); return a;");
        Assert.assertEquals(java.math.BigInteger.valueOf(3), list(r).get(0));
    }

    @Test
    public void arrayPopTest() {
        Assert.assertEquals(3L, run("var a = [1, 2, 3]; return arrayPop(a);").first().toInteger());
    }

    @Test
    public void arrayShiftTest() {
        Assert.assertEquals(1L, run("var a = [1, 2, 3]; return arrayShift(a);").first().toInteger());
    }

    @Test
    public void arrayUnshiftTest() {
        RulerResult r = run("var a = [2, 3]; arrayUnshift(a, 1); return a;");
        List<?> list = list(r);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(1), list.get(0));
    }

    @Test
    public void arrayIndexOfTest() {
        Assert.assertEquals(1L, run("return arrayIndexOf([1, 2, 3], 2);").first().toInteger());
        Assert.assertEquals(-1L, run("return arrayIndexOf([1, 2, 3], 5);").first().toInteger());
    }

    @Test
    public void arraySortTest() {
        RulerResult r = run("var a = [3, 1, 2]; arraySort(a); return a;");
        List<?> list = list(r);
        Assert.assertEquals(java.math.BigInteger.valueOf(1), list.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(3), list.get(2));
    }

    @Test
    public void arraySortComparatorTest() {
        RulerResult r = run("var a = [1, 2, 3]; arraySort(a, fun(a, b) { return b - a; }); return a;");
        List<?> list = list(r);
        Assert.assertEquals(java.math.BigInteger.valueOf(3), list.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(1), list.get(2));
    }

    // ==================== array object methods ====================

    @Test
    public void arrayObjectMethodsTest() {
        Assert.assertEquals(3L, run("return [1, 2, 3].length();").first().toInteger());
        Assert.assertEquals(4L, run("var a = [1, 2, 3]; a.push(4); return a.length();").first().toInteger());
        Assert.assertEquals(3L, run("var a = [1, 2, 3]; return a.pop();").first().toInteger());
        Assert.assertEquals(1L, run("var a = [1, 2, 3]; return a.shift();").first().toInteger());
        Assert.assertEquals(3L, run("var a = [2, 3]; a.unshift(1); return a.length();").first().toInteger());
        Assert.assertEquals(2L, run("return [1, 2, 3].slice(1, 3).length();").first().toInteger());
        Assert.assertEquals(4L, run("return [1, 2].concat([3, 4]).length();").first().toInteger());
        Assert.assertEquals("a-b", run("return ['a', 'b'].join('-');").first().toString());
        Assert.assertEquals(1L, run("return [3, 1, 2].sort()[0];").first().toInteger());
        Assert.assertEquals(1L, run("return [1, 2, 3].indexOf(2);").first().toInteger());
        Assert.assertEquals(4L, run("return [1, 2, 3].map(fun(x){return x+1;})[2];").first().toInteger());
        Assert.assertEquals(2L, run("return [1, 2, 3, 4].filter(fun(x){return x > 2;}).length();").first().toInteger());
        Assert.assertEquals(10L, run("return [1, 2, 3, 4].reduce(fun(a,b){return a+b;}, 0);").first().toInteger());
    }

    // ==================== object ====================

    @Test
    public void objectKeysTest() {
        RulerResult r = run("return keys({a: 1, b: 2});");
        Assert.assertEquals(2, list(r).size());
    }

    @Test
    public void objectValuesTest() {
        RulerResult r = run("return values({a: 1, b: 2});");
        List<?> list = list(r);
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(1), list.get(0));
    }

    @Test
    public void objectHasKeyTest() {
        Assert.assertTrue(run("return hasKey({a: 1}, 'a');").first().toBoolean());
        Assert.assertFalse(run("return hasKey({a: 1}, 'b');").first().toBoolean());
    }

    @Test
    public void objectMergeTest() {
        Assert.assertTrue(run("return hasKey(merge({a: 1}, {b: 2}), 'a');").first().toBoolean());
        Assert.assertTrue(run("return hasKey(merge({a: 1}, {b: 2}), 'b');").first().toBoolean());
    }

    // ==================== type ====================

    @Test
    public void typeCheckTest() {
        Assert.assertTrue(run("return isNull(null);").first().toBoolean());
        Assert.assertTrue(run("return isNumber(123);").first().toBoolean());
        Assert.assertTrue(run("return isString('abc');").first().toBoolean());
        Assert.assertTrue(run("return isBool(true);").first().toBoolean());
        Assert.assertTrue(run("return isArray([1, 2]);").first().toBoolean());
        Assert.assertTrue(run("return isFunction(fun() {});").first().toBoolean());
        Assert.assertTrue(run("return isDate(Datetime());").first().toBoolean());

        Assert.assertFalse(run("return isNull(0);").first().toBoolean());
        Assert.assertFalse(run("return isNumber('abc');").first().toBoolean());
        Assert.assertFalse(run("return isString(123);").first().toBoolean());
        Assert.assertFalse(run("return isBool(1);").first().toBoolean());
        Assert.assertFalse(run("return isArray({a: 1});").first().toBoolean());
        Assert.assertFalse(run("return isFunction(123);").first().toBoolean());
        Assert.assertFalse(run("return isDate('2024-01-01');").first().toBoolean());
    }

    // ==================== backward compatibility: sort.txt module ====================

    @Test
    public void sortModuleBackwardCompatTest() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [5, 1, 3]; sort.Sort(arr); return arr;";
        RulerResult r = Ruler.compile(script, configuration).run();
        Assert.assertEquals("[1, 3, 5]", r.first().toString());
    }

    // ==================== json module ====================

    @Test
    public void jsonParsePrimitivesTest() {
        Assert.assertNull(run("import '/ruler/std/json.txt' json; return json.parse('null');").first().getValue());
        Assert.assertTrue(run("import '/ruler/std/json.txt' json; return json.parse('true');").first().toBoolean());
        Assert.assertFalse(run("import '/ruler/std/json.txt' json; return json.parse('false');").first().toBoolean());
        Assert.assertEquals(42L, run("import '/ruler/std/json.txt' json; return json.parse('42');").first().toInteger());
        Assert.assertEquals(-3.14, run("import '/ruler/std/json.txt' json; return json.parse('-3.14');").first().toDouble(), 0.0001);
        Assert.assertEquals("hello", run("import '/ruler/std/json.txt' json; return json.parse('\"hello\"');").first().toString());
    }

    @Test
    public void jsonParseArrayTest() {
        RulerResult r = run("import '/ruler/std/json.txt' json; return json.parse('[1, 2, 3]');");
        List<?> list = list(r);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(java.math.BigInteger.valueOf(1), list.get(0));
        Assert.assertEquals(java.math.BigInteger.valueOf(3), list.get(2));
    }

    @Test
    public void jsonParseObjectTest() {
        String script = "import '/ruler/std/json.txt' json; var obj = json.parse('{\"a\": 1, \"b\": \"two\"}'); return obj.a;";
        Assert.assertEquals(1L, run(script).first().toInteger());
        script = "import '/ruler/std/json.txt' json; var obj = json.parse('{\"a\": 1, \"b\": \"two\"}'); return obj.b;";
        Assert.assertEquals("two", run(script).first().toString());
    }

    @Test
    public void jsonStringifyPrimitivesTest() {
        Assert.assertEquals("null", run("import '/ruler/std/json.txt' json; return json.stringify(null);").first().toString());
        Assert.assertEquals("true", run("import '/ruler/std/json.txt' json; return json.stringify(true);").first().toString());
        Assert.assertEquals("false", run("import '/ruler/std/json.txt' json; return json.stringify(false);").first().toString());
        Assert.assertEquals("42", run("import '/ruler/std/json.txt' json; return json.stringify(42);").first().toString());
        Assert.assertEquals("\"hello\"", run("import '/ruler/std/json.txt' json; return json.stringify(\"hello\");").first().toString());
    }

    @Test
    public void jsonStringifyArrayTest() {
        Assert.assertEquals("[1,2,3]", run("import '/ruler/std/json.txt' json; return json.stringify([1, 2, 3]);").first().toString());
    }

    @Test
    public void jsonStringifyObjectTest() {
        String result = run("import '/ruler/std/json.txt' json; return json.stringify({a: 1});").first().toString();
        Assert.assertEquals("{\"a\":1}", result);
    }

    @Test
    public void jsonRoundTripTest() {
        String script = "import '/ruler/std/json.txt' json; var s = json.stringify({x: [1, 2], y: \"test\"}); return json.stringify(json.parse(s));";
        String result = run(script).first().toString();
        Assert.assertTrue(result.contains("\"x\":[1,2]"));
        Assert.assertTrue(result.contains("\"y\":\"test\""));
    }

    @Test(expected = Exception.class)
    public void jsonParseTrailingCommaErrorTest() {
        run("import '/ruler/std/json.txt' json; return json.parse('[1, 2,]');");
    }

    @Test(expected = Exception.class)
    public void jsonParseLeadingZeroErrorTest() {
        run("import '/ruler/std/json.txt' json; return json.parse('01');");
    }

    @Test(expected = Exception.class)
    public void jsonStringifyFunctionErrorTest() {
        run("import '/ruler/std/json.txt' json; return json.stringify(fun(){});");
    }
}
