package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.StartConfig;
import com.kamijoucen.ruler.service.Main;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;

public class MainTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Object invokePrivate(String name, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = Main.class.getDeclaredMethod(name, paramTypes);
        method.setAccessible(true);
        try {
            return method.invoke(null, args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void parseConfigNoArgsTest() throws Exception {
        StartConfig config = (StartConfig) invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{});
        Assert.assertNull(config.getFilePath());
        Assert.assertEquals(0, config.getMaxLoopNumber());
        Assert.assertEquals(0, config.getMaxStackDepth());
    }

    @Test
    public void parseConfigValidArgsTest() throws Exception {
        StartConfig config = (StartConfig) invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{"-f=test.ruler", "-maxLoopNumber=100", "-maxStackDepth=50"});
        Assert.assertEquals("test.ruler", config.getFilePath());
        Assert.assertEquals(100, config.getMaxLoopNumber());
        Assert.assertEquals(50, config.getMaxStackDepth());
    }

    @Test
    public void parseConfigRepeatedKeyUsesLastValueTest() throws Exception {
        StartConfig config = (StartConfig) invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{"-maxLoopNumber=100", "-maxLoopNumber=200"});
        Assert.assertEquals(200, config.getMaxLoopNumber());
    }

    @Test
    public void parseConfigWindowsStylePathTest() throws Exception {
        StartConfig config = (StartConfig) invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{"-f=C:\\temp\\demo.ruler"});
        Assert.assertEquals("C:\\temp\\demo.ruler", config.getFilePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigIllegalArgNoDashTest() throws Exception {
        invokePrivate("parseConfig", new Class[]{String[].class}, (Object) new String[]{"foo"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigIllegalArgNoEqualTest() throws Exception {
        invokePrivate("parseConfig", new Class[]{String[].class}, (Object) new String[]{"-foo"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigUnknownKeyTest() throws Exception {
        invokePrivate("parseConfig", new Class[]{String[].class}, (Object) new String[]{"-unknown=value"});
    }

    @Test(expected = NumberFormatException.class)
    public void parseConfigInvalidIntegerTest() throws Exception {
        invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{"-maxLoopNumber=abc"});
    }

    @Test
    public void handleConfigPositiveValuesTest() throws Exception {
        StartConfig config = (StartConfig) invokePrivate("parseConfig", new Class[]{String[].class},
                (Object) new String[]{"-maxLoopNumber=200", "-maxStackDepth=100"});
        invokePrivate("handleConfig", new Class[]{StartConfig.class}, config);
    }

    @Test
    public void startFileSuccessTest() throws Exception {
        File tempFile = folder.newFile("test.ruler");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("return 1 + 1;");
        }

        StartConfig config = new StartConfig();
        config.setFilePath(tempFile.getAbsolutePath());
        invokePrivate("startFile", new Class[]{StartConfig.class}, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileNotExistsTest() throws Exception {
        StartConfig config = new StartConfig();
        config.setFilePath("/nonexistent/path/file.ruler");
        invokePrivate("startFile", new Class[]{StartConfig.class}, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileIsDirectoryTest() throws Exception {
        File tempDir = folder.newFolder("testdir");
        StartConfig config = new StartConfig();
        config.setFilePath(tempDir.getAbsolutePath());
        invokePrivate("startFile", new Class[]{StartConfig.class}, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileEmptyTest() throws Exception {
        File tempFile = folder.newFile("empty.ruler");
        StartConfig config = new StartConfig();
        config.setFilePath(tempFile.getAbsolutePath());
        invokePrivate("startFile", new Class[]{StartConfig.class}, config);
    }
}
