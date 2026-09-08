package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.api.Ruler;
import com.kamijoucen.ruler.logic.compiler.RulerCompiler;
import com.kamijoucen.ruler.logic.eval.RulerInterpreter;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class CallSemanticsTest {

    @Test
    public void mapAndFilterAcceptBoundClosureMethods() {
        RulerConfiguration configuration = new RulerConfiguration();
        String object = "var obj = { offset: 10, "
                + "add: fun(self, x) { return self.offset + x; }, "
                + "keep: fun(self, x) { return x > self.offset; } }; ";

        Assert.assertEquals(11L, Ruler.compile(object + "return obj.add(1);", configuration)
                .run().first().toInteger());
        Assert.assertEquals(Arrays.asList(BigInteger.valueOf(11), BigInteger.valueOf(12)),
                Ruler.compile(object + "return [1, 2].map(obj.add);", configuration)
                        .run().first().getValue());
        Assert.assertEquals(Collections.singletonList(BigInteger.valueOf(11)),
                Ruler.compile(object + "return [9, 11].filter(obj.keep);", configuration)
                        .run().first().getValue());
    }

    @Test
    public void mapAcceptsBoundNativeMethods() {
        Assert.assertEquals(Arrays.asList("h", "e"),
                Ruler.compile("return [0, 1].map('hello'.charAt);", new RulerConfiguration())
                        .run().first().getValue());
    }

    @Test
    public void rejectedCallDoesNotPoisonFollowingReplStatement() {
        RulerConfiguration configuration = new RulerConfiguration();
        configuration.setMaxStackDepth(1);
        RuntimeContext context = configuration.createDefaultRuntimeContext(null);
        Scope scope = new Scope("repl", false, configuration.getGlobalScope(), null);
        RulerModule recursive = RulerCompiler.compileStatement(
                new RulerScript("repl", "fun recursive() { return recursive(); } recursive();"),
                configuration);

        try {
            RulerInterpreter.runStatement(recursive, scope, context);
            Assert.fail("Expected the recursive call to exceed the depth limit");
        } catch (RulerRuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("Stack depth exceeded"));
        }
        Assert.assertEquals(0, context.getCallDepth().getDepth());

        RulerModule next = RulerCompiler.compileStatement(
                new RulerScript("repl", "fun next() { return 7; }"), configuration);
        RulerInterpreter.runStatement(next, scope, context);
        RulerModule call = RulerCompiler.compileStatement(new RulerScript("repl", "next();"), configuration);
        Assert.assertEquals(Collections.singletonList(BigInteger.valueOf(7)),
                RulerInterpreter.runStatement(call, scope, context));
        Assert.assertEquals(0, context.getCallDepth().getDepth());
    }
}
