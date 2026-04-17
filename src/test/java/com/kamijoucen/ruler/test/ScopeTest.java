package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class ScopeTest {

    @Test
    public void findLocalTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.putLocal("a", new IntegerValue(BigInteger.valueOf(10)));
        Assert.assertEquals(BigInteger.valueOf(10), ((IntegerValue) scope.find("a")).getValue());
    }

    @Test
    public void findParentTest() {
        Scope parent = new Scope("parent", false, null, null);
        parent.putLocal("a", new IntegerValue(BigInteger.valueOf(20)));
        Scope child = new Scope("child", false, parent, null);
        Assert.assertEquals(BigInteger.valueOf(20), ((IntegerValue) child.find("a")).getValue());
    }

    @Test
    public void findMissingTest() {
        Scope scope = new Scope("test", false, null, null);
        Assert.assertNull(scope.find("missing"));
    }

    @Test
    public void shadowingTest() {
        Scope parent = new Scope("parent", false, null, null);
        parent.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        Scope child = new Scope("child", false, parent, null);
        child.putLocal("a", new IntegerValue(BigInteger.valueOf(2)));
        Assert.assertEquals(BigInteger.valueOf(2), ((IntegerValue) child.find("a")).getValue());
        Assert.assertEquals(BigInteger.valueOf(1), ((IntegerValue) parent.find("a")).getValue());
    }

    @Test
    public void updateLocalTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        scope.update("a", new IntegerValue(BigInteger.valueOf(99)));
        Assert.assertEquals(BigInteger.valueOf(99), ((IntegerValue) scope.find("a")).getValue());
    }

    @Test
    public void updateParentTest() {
        Scope parent = new Scope("parent", false, null, null);
        parent.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        Scope child = new Scope("child", false, parent, null);
        child.update("a", new IntegerValue(BigInteger.valueOf(99)));
        Assert.assertEquals(BigInteger.valueOf(99), ((IntegerValue) parent.find("a")).getValue());
    }

    @Test(expected = RulerRuntimeException.class)
    public void updateMissingTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.update("missing", new IntegerValue(BigInteger.valueOf(1)));
    }

    @Test
    public void defineLocalTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.defineLocal("a", new IntegerValue(BigInteger.valueOf(5)));
        Assert.assertEquals(BigInteger.valueOf(5), ((IntegerValue) scope.getByLocal("a")).getValue());
    }

    @Test(expected = RulerRuntimeException.class)
    public void defineLocalDuplicateTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.defineLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        scope.defineLocal("a", new IntegerValue(BigInteger.valueOf(2)));
    }

    @Test
    public void getByLocalOnlyTest() {
        Scope parent = new Scope("parent", false, null, null);
        parent.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        Scope child = new Scope("child", false, parent, null);
        Assert.assertNull(child.getByLocal("a"));
    }

    @Test
    public void removeTest() {
        Scope scope = new Scope("test", false, null, null);
        scope.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        scope.remove("a");
        Assert.assertNull(scope.find("a"));
    }

    @Test
    public void removeOnlyLocalTest() {
        Scope parent = new Scope("parent", false, null, null);
        parent.putLocal("a", new IntegerValue(BigInteger.valueOf(1)));
        Scope child = new Scope("child", false, parent, null);
        child.putLocal("a", new IntegerValue(BigInteger.valueOf(2)));
        child.remove("a");
        Assert.assertEquals(BigInteger.valueOf(1), ((IntegerValue) child.find("a")).getValue());
    }

    @Test
    public void isCallScopeTest() {
        Scope scope = new Scope("test", true, null, new TokenLocation(1, 1, "test.ruler"));
        Assert.assertTrue(scope.isCallScope());
        Assert.assertEquals("test.ruler", scope.getCallLocation().fileName);
    }

    @Test
    public void getStackNameTest() {
        Scope scope = new Scope("myStack", false, null, null);
        Assert.assertEquals("myStack", scope.getStackName());
    }
}
