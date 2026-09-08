package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.api.RulerRunner;
import com.kamijoucen.ruler.types.parameter.RulerResult;
import com.kamijoucen.ruler.types.module.ConfigModule;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.value.BaseValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class ImportAndModuleTest {

    private RulerConfiguration configuration;

    @Before
    public void init() {
        configuration = new RulerConfiguration();
    }

    private RulerRunner compile(String text) {
        return Ruler.compile(text, configuration);
    }

    // ---------- standard library import with assertion ----------

    @Test
    public void testSpiIoModuleReadsTemporaryFile() throws Exception {
        Path file = Files.createTempFile("ruler-spi-io-", ".txt");
        try {
            Files.write(file, "SPI module contents".getBytes(StandardCharsets.UTF_8));
            RulerResult result = compile("import 'io' io; return io.ReadAll($path);")
                    .run(Collections.singletonMap("path", file.toString()));
            Assert.assertEquals("SPI module contents", result.first().toString());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void testHostFunctionModuleCallsAndReusesCompiledModule() {
        AtomicInteger calls = new AtomicInteger();
        RulerFunction function = new RulerFunction() {
            @Override
            public String getName() {
                return "Increment";
            }

            @Override
            public Object call(RuntimeContext context, Scope scope, BaseValue self, Object... params) {
                calls.incrementAndGet();
                return ((Number) params[0]).longValue() + 1;
            }
        };
        configuration.getModules().register(ConfigModule.createFunctionModule(
                "host-math", Collections.singletonList(function)));
        RulerRunner runner = compile("import 'host-math' math; return math.Increment($value);");

        Assert.assertEquals(5L, runner.run(Collections.singletonMap("value", 4)).first().toInteger());
        RulerModule cached = configuration.getModules().findCached("host-math");
        Assert.assertNotNull(cached);
        Assert.assertEquals(10L, runner.run(Collections.singletonMap("value", 9)).first().toInteger());
        Assert.assertSame(cached, configuration.getModules().findCached("host-math"));
        Assert.assertEquals(2, calls.get());
    }

    @Test
    public void testImportSortModule() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [5, 1, 3]; sort.Sort(arr); return arr;";
        RulerResult r = compile(script).run();
        Assert.assertEquals("[1, 3, 5]", r.first().toString());
    }

    @Test
    public void testImportCollectionsModule() {
        // Contains(obj, list) -- obj first, list second
        String script = "import '/ruler/std/collections.txt' listUtil; return listUtil.Contains(2, [1, 2, 3]);";
        RulerResult r = compile(script).run();
        Assert.assertTrue(r.first().toBoolean());
    }

    // ---------- module caching ----------

    @Test
    public void testModuleCacheIsUsed() {
        final AtomicInteger loadCount = new AtomicInteger(0);
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "cached_module".equals(path);
            }
            @Override
            public String load(String path) {
                loadCount.incrementAndGet();
                return "var value = 42;";
            }
        });
        String script = "import 'cached_module' m; return m.value;";
        RulerResult r1 = compile(script).run();
        Assert.assertEquals(42L, r1.first().toInteger());
        RulerResult r2 = compile(script).run();
        Assert.assertEquals(42L, r2.first().toInteger());
        // Loader should only be invoked once because of caching.
        Assert.assertEquals(1, loadCount.get());
    }

    @Test
    public void testCachedModuleUsesFreshRuntimeStatePerImportAlias() {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "counter_module".equals(path);
            }

            @Override
            public String load(String path) {
                return "var counter = 0; fun Next() { counter = counter + 1; return counter; }";
            }
        });

        String script = "import 'counter_module' a; import 'counter_module' b; a.Next(); return b.Next();";
        RulerResult result = compile(script).run();
        Assert.assertEquals(1L, result.first().toInteger());
    }

    // ---------- import infix ----------

    @Test
    public void testImportInfix() {
        // Register a module via custom loader so it can be imported by a predictable path.
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return "infix_power".equals(path);
            }
            @Override
            public String load(String path) {
                return "infix fun pow(a, b) { var r = 1; var i = 0; while i < b { r = r * a; i = i + 1; } return r; }";
            }
        });
        String script = "import infix 'infix_power' p; return 2 pow 3;";
        RulerResult r = compile(script).run();
        Assert.assertEquals(8L, r.first().toInteger());
    }

    // ---------- global import registration ----------

    @Test
    public void testGlobalImportScriptModule() {
        RulerConfiguration cfg = new RulerConfiguration();
        cfg.registerGlobalImportScriptModule("var answer = 42;", "ans");
        String script = "return ans.answer;";
        RulerResult r = Ruler.compile(script, cfg).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    @Test
    public void testGlobalImportPathModule() {
        RulerConfiguration cfg = new RulerConfiguration();
        cfg.registerGlobalImportPathModule("/ruler/std/collections.txt", "listUtil");
        String script = "return listUtil.Contains(2, [1, 2]);";
        RulerResult r = Ruler.compile(script, cfg).run();
        Assert.assertTrue(r.first().toBoolean());
    }
}
