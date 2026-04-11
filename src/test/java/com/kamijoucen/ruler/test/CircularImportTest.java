package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.component.option.CustomImportLoader;
import com.kamijoucen.ruler.service.Ruler;
import com.kamijoucen.ruler.service.RulerRunner;
import org.junit.Assert;
import org.junit.Test;

public class CircularImportTest {

    @Test
    public void circularImportStackOverflowTest() {
        RulerConfigurationImpl configuration = new RulerConfigurationImpl();
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return path.startsWith("/test/");
            }

            @Override
            public String load(String path) {
                if ("/test/a".equals(path)) {
                    return "import \"/test/b\" b; return 1;";
                }
                if ("/test/b".equals(path)) {
                    return "import \"/test/a\" a; return 2;";
                }
                return null;
            }
        });

        RulerRunner runner = Ruler.compileScript("import \"/test/a\" a; return a;", configuration);
        try {
            runner.run();
            Assert.fail("Expected StackOverflowError for circular import");
        } catch (StackOverflowError e) {
            // expected
        }
    }
}
