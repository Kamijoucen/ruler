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

    @Test
    public void parseConfigNoArgsTest() throws Exception {
        Method method = Main.class.getDeclaredMethod("parseConfig", String[].class);
        method.setAccessible(true);
        StartConfig config = (StartConfig) method.invoke(null, (Object) new String[]{});
        Assert.assertNull(config.getFilePath());
        Assert.assertEquals(0, config.getMaxLoopNumber());
        Assert.assertEquals(0, config.getMaxStackDepth());
    }

    @Test
    public void parseConfigValidArgsTest() throws Exception {
        Method method = Main.class.getDeclaredMethod("parseConfig", String[].class);
        method.setAccessible(true);
        StartConfig config = (StartConfig) method.invoke(null, (Object) new String[]{"-f=test.ruler", "-maxLoopNumber=100", "-maxStackDepth=50"});
        Assert.assertEquals("test.ruler", config.getFilePath());
        Assert.assertEquals(100, config.getMaxLoopNumber());
        Assert.assertEquals(50, config.getMaxStackDepth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigIllegalArgNoDashTest() throws Exception {
        Method method = Main.class.getDeclaredMethod("parseConfig", String[].class);
        method.setAccessible(true);
        try {
            method.invoke(null, (Object) new String[]{"foo"});
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigIllegalArgNoEqualTest() throws Exception {
        Method method = Main.class.getDeclaredMethod("parseConfig", String[].class);
        method.setAccessible(true);
        try {
            method.invoke(null, (Object) new String[]{"-foo"});
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseConfigUnknownKeyTest() throws Exception {
        Method method = Main.class.getDeclaredMethod("parseConfig", String[].class);
        method.setAccessible(true);
        try {
            method.invoke(null, (Object) new String[]{"-unknown=value"});
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test
    public void handleConfigPositiveValuesTest() throws Exception {
        Method parseConfig = Main.class.getDeclaredMethod("parseConfig", String[].class);
        parseConfig.setAccessible(true);
        StartConfig config = (StartConfig) parseConfig.invoke(null, (Object) new String[]{"-maxLoopNumber=200", "-maxStackDepth=100"});

        Method handleConfig = Main.class.getDeclaredMethod("handleConfig", StartConfig.class);
        handleConfig.setAccessible(true);
        handleConfig.invoke(null, config);
    }

    @Test
    public void startFileSuccessTest() throws Exception {
        File tempFile = folder.newFile("test.ruler");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("return 1 + 1;");
        }

        Method startFile = Main.class.getDeclaredMethod("startFile", StartConfig.class);
        startFile.setAccessible(true);
        StartConfig config = new StartConfig();
        config.setFilePath(tempFile.getAbsolutePath());
        startFile.invoke(null, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileNotExistsTest() throws Exception {
        Method startFile = Main.class.getDeclaredMethod("startFile", StartConfig.class);
        startFile.setAccessible(true);
        StartConfig config = new StartConfig();
        config.setFilePath("/nonexistent/path/file.ruler");
        try {
            startFile.invoke(null, config);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileIsDirectoryTest() throws Exception {
        File tempDir = folder.newFolder("testdir");
        Method startFile = Main.class.getDeclaredMethod("startFile", StartConfig.class);
        startFile.setAccessible(true);
        StartConfig config = new StartConfig();
        config.setFilePath(tempDir.getAbsolutePath());
        try {
            startFile.invoke(null, config);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void startFileEmptyTest() throws Exception {
        File tempFile = folder.newFile("empty.ruler");
        Method startFile = Main.class.getDeclaredMethod("startFile", StartConfig.class);
        startFile.setAccessible(true);
        StartConfig config = new StartConfig();
        config.setFilePath(tempFile.getAbsolutePath());
        try {
            startFile.invoke(null, config);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }
}
