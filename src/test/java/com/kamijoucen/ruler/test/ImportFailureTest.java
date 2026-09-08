package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.api.Ruler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ImportFailureTest {

    private RulerConfiguration configuration;

    @Before
    public void init() {
        configuration = new RulerConfiguration();
    }

    private void registerModules(final Map<String, String> modules) {
        configuration.getModules().registerLoader(new CustomImportLoader() {
            @Override
            public boolean match(String path) {
                return modules.containsKey(path);
            }

            @Override
            public String load(String path) {
                return modules.get(path);
            }
        });
    }

    @Test
    public void missingImportThrowsHelpfulMessageTest() {
        try {
            Ruler.compile("import 'missing_module' m; return m;", configuration).run();
            Assert.fail("Expected RulerRuntimeException");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("missing_module"));
        }
    }

    @Test
    public void nestedImportFailureIncludesImportChainTest() {
        Map<String, String> modules = new HashMap<>();
        modules.put("a", "import 'b' b; return 1;");
        modules.put("b", "import 'missing' m; return 2;");
        registerModules(modules);

        try {
            Ruler.compile("import 'a' a; return a;", configuration).run();
            Assert.fail("Expected RulerRuntimeException");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("module 'missing' not found"));
            Assert.assertTrue(e.getMessage().contains("import chain: a -> b -> missing"));
        }
    }

    @Test
    public void duplicateAliasFailsAtCompileTimeTest() {
        Map<String, String> modules = new HashMap<>();
        modules.put("m1", "var value = 1;");
        modules.put("m2", "var value = 2;");
        registerModules(modules);

        try {
            Ruler.compile("import 'm1' dup; import 'm2' dup; return 0;", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("duplicate alias"));
        }
    }

    @Test
    public void duplicateAliaslessInfixImportFailsTest() {
        Map<String, String> modules = new HashMap<>();
        modules.put("infix_a", "infix fun addA(a, b) { return a + b; }");
        modules.put("infix_b", "infix fun addB(a, b) { return a + b; }");
        registerModules(modules);

        try {
            Ruler.compile("import infix 'infix_a'; import infix 'infix_b'; return 1;", configuration);
            Assert.fail("Expected SyntaxException");
        } catch (SyntaxException e) {
            Assert.assertTrue(e.getMessage().contains("duplicate alias"));
        }
    }
}
