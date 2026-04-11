package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.component.ImportCacheManager;
import com.kamijoucen.ruler.component.StackDepthCheckOperation;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.TypeScope;
import com.kamijoucen.ruler.domain.value.IntegerValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class RuntimeContextTest {

    private RuntimeContext context;

    @Before
    public void init() {
        RulerConfigurationImpl config = new RulerConfigurationImpl();
        context = new RuntimeContext(
                config.getEvalVisitor(),
                config.getTypeCheckVisitor(),
                new ImportCacheManager(),
                new StackDepthCheckOperation(),
                config
        );
    }

    @Test
    public void breakFlagLifecycleTest() {
        Assert.assertFalse(context.isBreakFlag());
        context.setBreakFlag(true);
        Assert.assertTrue(context.isBreakFlag());
        context.setBreakFlag(false);
        Assert.assertFalse(context.isBreakFlag());
    }

    @Test
    public void continueFlagLifecycleTest() {
        Assert.assertFalse(context.isContinueFlag());
        context.setContinueFlag(true);
        Assert.assertTrue(context.isContinueFlag());
        context.setContinueFlag(false);
        Assert.assertFalse(context.isContinueFlag());
    }

    @Test
    public void returnFlagLifecycleTest() {
        Assert.assertFalse(context.isReturnFlag());
        context.setReturnFlag(true);
        Assert.assertTrue(context.isReturnFlag());
        context.setReturnFlag(false);
        Assert.assertFalse(context.isReturnFlag());
    }

    @Test
    public void returnSpaceEmptyTest() {
        Assert.assertFalse(context.hasReturnValue());
        Assert.assertNull(context.getReturnSpace());
    }

    @Test
    public void setReturnSpaceTest() {
        context.setReturnSpace(Arrays.asList(new IntegerValue(1), new IntegerValue(2)));
        Assert.assertTrue(context.hasReturnValue());
        Assert.assertEquals(2, context.getReturnSpace().size());
    }

    @Test
    public void addReturnSpaceTest() {
        context.addReturnSpace(new IntegerValue(10));
        Assert.assertTrue(context.hasReturnValue());
        Assert.assertEquals(1, context.getReturnSpace().size());
        Assert.assertEquals(10, ((IntegerValue) context.getReturnSpace().get(0)).getValue());

        context.addReturnSpace(new IntegerValue(20));
        Assert.assertEquals(2, context.getReturnSpace().size());
    }

    @Test
    public void clearReturnSpaceTest() {
        context.addReturnSpace(new IntegerValue(1));
        Assert.assertTrue(context.hasReturnValue());
        context.clearReturnSpace();
        Assert.assertFalse(context.hasReturnValue());
        Assert.assertNull(context.getReturnSpace());
    }

    @Test
    public void findOutValueExistingTest() {
        context.setOutSpace(Collections.singletonMap("score", new IntegerValue(85)));
        Assert.assertEquals(85, ((IntegerValue) context.findOutValue("score")).getValue());
    }

    @Test
    public void findOutValueMissingTest() {
        context.setOutSpace(Collections.emptyMap());
        Assert.assertSame(NullValue.INSTANCE, context.findOutValue("missing"));
    }

    @Test
    public void setOutSpaceNullTest() {
        context.setOutSpace(null);
        Assert.assertSame(NullValue.INSTANCE, context.findOutValue("any"));
    }

    @Test
    public void typeScopeTest() {
        TypeScope typeScope = new TypeScope(null);
        context.setTypeScope(typeScope);
        Assert.assertSame(typeScope, context.getTypeScope());
    }
}
