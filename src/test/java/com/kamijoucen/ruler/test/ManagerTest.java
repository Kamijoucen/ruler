package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.component.ImportCacheManager;
import com.kamijoucen.ruler.component.IntegerNumberCacheImpl;
import com.kamijoucen.ruler.component.ValueConvertManagerImpl;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.domain.value.convert.ValueConvert;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

public class ManagerTest {

    @Test
    public void importCacheManagerBlankPathTest() {
        ImportCacheManager manager = new ImportCacheManager();
        Assert.assertNull(manager.getImportModule(""));
        Assert.assertNull(manager.getImportModule(null));
    }

    @Test
    public void importCacheManagerPutGetTest() {
        ImportCacheManager manager = new ImportCacheManager();
        RulerModule module = new RulerModule("test");
        manager.putImportModule("/path/to/module", module);
        Assert.assertSame(module, manager.getImportModule("/path/to/module"));
    }

    @Test
    public void importCacheManagerGetAllTest() {
        ImportCacheManager manager = new ImportCacheManager();
        RulerModule module1 = new RulerModule("test1");
        RulerModule module2 = new RulerModule("test2");
        manager.putImportModule("/a", module1);
        manager.putImportModule("/b", module2);
        Assert.assertEquals(2, manager.getAllImportModule().size());
    }

    @Test
    public void valueConvertManagerNullTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        ValueConvert convert = manager.getConverter((Object) null);
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.NULL, convert.getType());
    }

    @Test
    public void valueConvertManagerArrayTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        ValueConvert convert = manager.getConverter(new int[]{1, 2, 3});
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.ARRAY, convert.getType());
    }

    @Test
    public void valueConvertManagerMapTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        ValueConvert convert = manager.getConverter(new HashMap<String, Object>());
        Assert.assertNotNull(convert);
        Assert.assertEquals(ValueType.RSON, convert.getType());
    }

    @Test
    public void valueConvertManagerSupportedTypesTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        Assert.assertEquals(ValueType.INTEGER, manager.getConverter(42).getType());
        Assert.assertEquals(ValueType.INTEGER, manager.getConverter(42L).getType());
        Assert.assertEquals(ValueType.DOUBLE, manager.getConverter(3.14).getType());
        Assert.assertEquals(ValueType.DOUBLE, manager.getConverter(3.14f).getType());
        Assert.assertEquals(ValueType.STRING, manager.getConverter("hello").getType());
        Assert.assertEquals(ValueType.BOOL, manager.getConverter(true).getType());
        Assert.assertEquals(ValueType.DATE, manager.getConverter(new Date()).getType());
    }

    @Test
    public void valueConvertManagerUnknownTypeTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        Assert.assertNull(manager.getConverter(new Object()));
    }

    @Test
    public void valueConvertManagerByValueTypeTest() {
        ValueConvertManagerImpl manager = new ValueConvertManagerImpl();
        Assert.assertNotNull(manager.getConverter(ValueType.INTEGER));
        Assert.assertNotNull(manager.getConverter(ValueType.STRING));
        Assert.assertNull(manager.getConverter(ValueType.FUNCTION));
    }

    @Test
    public void integerNumberCacheHitTest() {
        IntegerNumberCacheImpl cache = new IntegerNumberCacheImpl();
        Assert.assertSame(cache.getValue(0), cache.getValue(0));
        Assert.assertSame(cache.getValue(1023), cache.getValue(1023));
    }

    @Test
    public void integerNumberCacheMissTest() {
        IntegerNumberCacheImpl cache = new IntegerNumberCacheImpl();
        Assert.assertNotSame(cache.getValue(1024), cache.getValue(1024));
        Assert.assertNotSame(cache.getValue(-1), cache.getValue(-1));
    }

    @Test
    public void integerNumberCacheValueTest() {
        IntegerNumberCacheImpl cache = new IntegerNumberCacheImpl();
        Assert.assertEquals(100, cache.getValue(100).getValue());
        Assert.assertEquals(2048, cache.getValue(2048).getValue());
        Assert.assertEquals(-100, cache.getValue(-100).getValue());
    }
}
