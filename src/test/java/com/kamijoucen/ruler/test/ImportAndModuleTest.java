package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.config.option.CustomImportLoader;
import com.kamijoucen.ruler.config.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.module.RulerRunner;
import com.kamijoucen.ruler.parameter.RulerResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ImportAndModuleTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private RulerRunner compileScript(String text) {
        return Ruler.compileScript(text, configuration);
    }

    // ---------- standard library import with assertion ----------

    @Test
    public void testImportSortModule() {
        String script = "import '/ruler/std/sort.txt' sort; var arr = [5, 1, 3]; sort.Sort(arr); return arr;";
        RulerResult r = compileScript(script).run();
        Assert.assertEquals("[1, 3, 5]", r.first().toString());
    }

    @Test
    public void testImportCollectionsModule() {
        // Contains(obj, list) -- obj first, list second
        String script = "import '/ruler/std/collections.txt' listUtil; return listUtil.Contains(2, [1, 2, 3]);";
        RulerResult r = compileScript(script).run();
        Assert.assertTrue(r.first().toBoolean());
    }

    // ---------- module caching ----------

    @Test
    public void testModuleCacheIsUsed() {
        final AtomicInteger loadCount = new AtomicInteger(0);
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
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
        RulerResult r1 = compileScript(script).run();
        Assert.assertEquals(42L, r1.first().toInteger());
        RulerResult r2 = compileScript(script).run();
        Assert.assertEquals(42L, r2.first().toInteger());
        // Loader should only be invoked once because of caching.
        Assert.assertEquals(1, loadCount.get());
    }

    // ---------- import infix ----------

    @Test
    public void testImportInfix() {
        // Register a module via custom loader so it can be imported by a predictable path.
        configuration.getCustomImportLoadManager().registerCustomImportLoader(new CustomImportLoader() {
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
        RulerResult r = compileScript(script).run();
        Assert.assertEquals(8L, r.first().toInteger());
    }

    // ---------- global import registration ----------

    @Test
    public void testGlobalImportScriptModule() {
        RulerConfigurationImpl cfg = new RulerConfigurationImpl();
        cfg.registerGlobalImportScriptModule("var answer = 42;", "ans");
        String script = "return ans.answer;";
        RulerResult r = Ruler.compileScript(script, cfg).run();
        Assert.assertEquals(42L, r.first().toInteger());
    }

    @Test
    public void testGlobalImportPathModule() {
        RulerConfigurationImpl cfg = new RulerConfigurationImpl();
        cfg.registerGlobalImportPathModule("/ruler/std/global.txt", "op");
        String script = "return op.Add(1, 2);";
        RulerResult r = Ruler.compileScript(script, cfg).run();
        Assert.assertEquals(3L, r.first().toInteger());
    }
}
