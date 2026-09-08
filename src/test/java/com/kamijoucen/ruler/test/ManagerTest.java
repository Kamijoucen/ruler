package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.module.ModuleState;
import com.kamijoucen.ruler.types.value.IntegerValue;
import com.kamijoucen.ruler.logic.convert.ValueConversions;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.types.value.ValueConvert;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

public class ManagerTest {

    @Test
    public void moduleCacheBlankPathTest() {
        ModuleState manager = new ModuleState();
        Assert.assertNull(manager.findCached(""));
        Assert.assertNull(manager.findCached(null));
    }

    @Test
    public void moduleCachePutGetTest() {
        ModuleState manager = new ModuleState();
        RulerModule module = new RulerModule("test");
        manager.cache("/path/to/module", module);
        Assert.assertSame(module, manager.findCached("/path/to/module"));
    }

    @Test
    public void moduleCacheGetAllTest() {
        ModuleState manager = new ModuleState();
        RulerModule module1 = new RulerModule("test1");
        RulerModule module2 = new RulerModule("test2");
        manager.cache("/a", module1);
        manager.cache("/b", module2);
        Assert.assertEquals(2, manager.getCachedModules().size());
    }

    @Test
    public void valueConvertManagerNullTest() {
        ValueConvert convert = ValueConversions.getConverter((Object) null);
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.NULL, convert.getType());
    }

    @Test
    public void valueConvertManagerArrayTest() {
        ValueConvert convert = ValueConversions.getConverter(new int[]{1, 2, 3});
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.ARRAY, convert.getType());
    }

    @Test
    public void valueConvertManagerMapTest() {
        ValueConvert convert = ValueConversions.getConverter(new HashMap<String, Object>());
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.RSON, convert.getType());
    }

    @Test
    public void valueConvertManagerSupportedTypesTest() {
        Assert.assertEquals(ValueType.INTEGER, ValueConversions.getConverter(42).getType());
        Assert.assertEquals(ValueType.INTEGER, ValueConversions.getConverter(42L).getType());
        Assert.assertEquals(ValueType.DOUBLE, ValueConversions.getConverter(3.14).getType());
        Assert.assertEquals(ValueType.DOUBLE, ValueConversions.getConverter(3.14f).getType());
        Assert.assertEquals(ValueType.STRING, ValueConversions.getConverter("hello").getType());
        Assert.assertEquals(ValueType.BOOL, ValueConversions.getConverter(true).getType());
        Assert.assertEquals(ValueType.DATE, ValueConversions.getConverter(new Date()).getType());
    }

    @Test
    public void valueConvertManagerUnknownTypeTest() {
        Assert.assertNull(ValueConversions.getConverter(new Object()));
    }

    @Test
    public void valueConvertManagerByValueTypeTest() {
        Assert.assertNotNull(ValueConversions.getConverter(ValueType.INTEGER));
        Assert.assertNotNull(ValueConversions.getConverter(ValueType.STRING));
        Assert.assertNull(ValueConversions.getConverter(ValueType.FUNCTION));
    }

    @Test
    public void integerNumberCacheHitTest() {
        Assert.assertSame(IntegerValue.valueOf(BigInteger.valueOf(0)), IntegerValue.valueOf(BigInteger.valueOf(0)));
        Assert.assertSame(IntegerValue.valueOf(BigInteger.valueOf(1023)), IntegerValue.valueOf(BigInteger.valueOf(1023)));
    }

    @Test
    public void integerNumberCacheMissTest() {
        Assert.assertNotSame(IntegerValue.valueOf(BigInteger.valueOf(1024)), IntegerValue.valueOf(BigInteger.valueOf(1024)));
        Assert.assertNotSame(IntegerValue.valueOf(BigInteger.valueOf(-1)), IntegerValue.valueOf(BigInteger.valueOf(-1)));
    }

    @Test
    public void integerNumberCacheValueTest() {
        Assert.assertEquals(BigInteger.valueOf(100), IntegerValue.valueOf(BigInteger.valueOf(100)).getValue());
        Assert.assertEquals(BigInteger.valueOf(2048), IntegerValue.valueOf(BigInteger.valueOf(2048)).getValue());
        Assert.assertEquals(BigInteger.valueOf(-100), IntegerValue.valueOf(BigInteger.valueOf(-100)).getValue());
    }
}
