package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationTest {

    @Test(expected = NotSerializableException.class)
    public void rulerRunnerSerializationNotSupportedTest() throws Exception {
        RulerConfigurationImpl config1 = new RulerConfigurationImpl();
        RulerRunner original = Ruler.compileScript("return 42;", config1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
    }

    @Test
    public void rulerRunnerHoldsModuleReferenceTest() {
        RulerConfigurationImpl config = new RulerConfigurationImpl();
        RulerRunner runner = Ruler.compileScript("return 42;", config);
        Assert.assertNotNull(runner.getModule());
        Assert.assertTrue(runner.isScript());
    }
}
