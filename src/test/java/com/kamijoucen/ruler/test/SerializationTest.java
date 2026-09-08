package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.api.RulerRunner;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class SerializationTest {

    @Test(expected = NotSerializableException.class)
    public void rulerRunnerSerializationNotSupportedTest() throws Exception {
        RulerConfiguration config1 = new RulerConfiguration();
        RulerRunner original = Ruler.compile("return 42;", config1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
    }

    @Test
    public void rulerRunnerHoldsModuleReferenceTest() {
        RulerConfiguration config = new RulerConfiguration();
        RulerRunner runner = Ruler.compile("return 42;", config);
        Assert.assertNotNull(runner.getModule());
    }
}
