package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.module.ConfigModule;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.api.Ruler;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class CircularImportTest {

    @Test
    public void nestedIndependentRunHasItsOwnImportChain() {
        RulerConfiguration configuration = new RulerConfiguration();
        AtomicBoolean runNested = new AtomicBoolean(true);
        configuration.getModules().register(ConfigModule.createScriptModule(
                "/test/shared", "var value = RunNested();"));
        configuration.registerGlobalFunction(new RulerFunction() {
            @Override
            public String getName() {
                return "RunNested";
            }

            @Override
            public Object call(RuntimeContext context, Scope scope, BaseValue self, Object... params) {
                if (runNested.getAndSet(false)) {
                    return Ruler.compile("import '/test/shared' shared; return shared.value;", configuration)
                            .run().first().toInteger();
                }
                return 9L;
            }
        });

        Assert.assertEquals(9L,
                Ruler.compile("import '/test/shared' shared; return shared.value;", configuration)
                        .run().first().toInteger());
    }

    @Test
    public void circularImportShouldFailFastTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        configuration.getModules().registerLoader(new CustomImportLoader() {
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

        try {
            Ruler.compile("import \"/test/a\" a; return a;", configuration).run();
            Assert.fail("Expected a controlled circular-import exception");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("circular import"));
            Assert.assertTrue(e.getMessage().contains("/test/a -> /test/b -> /test/a"));
        }
    }

    @Test
    public void selfCircularImportShouldFailFastTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "/test/self".equals(path);
            }

            @Override
            public String load(String path) {
                return "import \"/test/self\" self; return 1;";
            }
        });

        try {
            Ruler.compile("import \"/test/self\" self; return self;", configuration).run();
            Assert.fail("Expected a controlled circular-import exception");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("/test/self -> /test/self"));
        }
    }

    @Test
    public void circularImportFailureDoesNotPoisonNextImportTest() {
        RulerConfiguration configuration = new RulerConfiguration();
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return path.startsWith("/test/");
            }

            @Override
            public String load(String path) {
                if ("/test/self".equals(path)) {
                    return "import \"/test/self\" self; return 1;";
                }
                if ("/test/ok".equals(path)) {
                    return "var value = 3;";
                }
                return null;
            }
        });

        try {
            Ruler.compile("import \"/test/self\" self; return self;", configuration).run();
            Assert.fail("Expected a controlled circular-import exception");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("circular import"));
        }

        Assert.assertEquals(3L,
                Ruler.compile("import \"/test/ok\" ok; return ok.value;", configuration)
                        .run()
                        .first()
                        .toInteger());
    }
}
