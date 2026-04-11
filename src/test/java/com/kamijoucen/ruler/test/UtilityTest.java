package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.logic.util.ConvertUtil;
import com.kamijoucen.ruler.logic.util.SyntaxCheckUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class UtilityTest {

    @Test
    public void syntaxCheckUtilIsNumberTest() {
        Assert.assertTrue(SyntaxCheckUtil.isNumber(ValueType.INTEGER));
        Assert.assertTrue(SyntaxCheckUtil.isNumber(ValueType.DOUBLE));
        Assert.assertFalse(SyntaxCheckUtil.isNumber(ValueType.STRING));
        Assert.assertFalse(SyntaxCheckUtil.isNumber(ValueType.BOOL));
    }

    @Test(expected = SyntaxException.class)
    public void syntaxCheckUtilImportPathEmptyTest() {
        SyntaxCheckUtil.importPathCheck(new String[]{});
    }

    @Test(expected = SyntaxException.class)
    public void syntaxCheckUtilImportPathNullTest() {
        SyntaxCheckUtil.importPathCheck(null);
    }

    @Test(expected = SyntaxException.class)
    public void syntaxCheckUtilImportPathBlankSegmentTest() {
        SyntaxCheckUtil.importPathCheck(new String[]{"valid", ""});
    }

    @Test(expected = SyntaxException.class)
    public void syntaxCheckUtilImportPathInvalidCharTest() {
        SyntaxCheckUtil.importPathCheck(new String[]{"hello*world"});
    }

    @Test
    public void syntaxCheckUtilImportPathValidTest() {
        SyntaxCheckUtil.importPathCheck(new String[]{"ruler", "std", "globaltxt"});
    }

    @Test(expected = SyntaxException.class)
    public void syntaxCheckUtilDuplicateAliasTest() {
        List<ImportNode> imports = new ArrayList<>();
        imports.add(new ImportNode("path1", "alias", false, null));
        imports.add(new ImportNode("path2", "alias", false, null));
        SyntaxCheckUtil.availableImport(imports);
    }

    @Test
    public void syntaxCheckUtilUniqueAliasTest() {
        List<ImportNode> imports = new ArrayList<>();
        imports.add(new ImportNode("path1", "a", false, null));
        imports.add(new ImportNode("path2", "b", false, null));
        SyntaxCheckUtil.availableImport(imports);
    }

    @Test
    public void convertUtilParseToNumberValidTest() {
        Assert.assertEquals(123, ConvertUtil.parseToNumber("123").intValue());
        Assert.assertEquals(123.45, ConvertUtil.parseToNumber("123.45").doubleValue(), 0.001);
    }

    @Test
    public void convertUtilParseToNumberInvalidTest() {
        Assert.assertNull(ConvertUtil.parseToNumber("abc"));
        Assert.assertNull(ConvertUtil.parseToNumber(""));
    }

    @Test
    public void collectionUtilMapEmptyTest() {
        Assert.assertTrue(CollectionUtil.isEmpty((Map<?, ?>) null));
        Assert.assertTrue(CollectionUtil.isEmpty(Collections.emptyMap()));
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        Assert.assertFalse(CollectionUtil.isEmpty(map));
    }

    @Test
    public void collectionUtilCollectionEmptyTest() {
        Assert.assertTrue(CollectionUtil.isEmpty((Collection<?>) null));
        Assert.assertTrue(CollectionUtil.isEmpty(Collections.emptyList()));
        Assert.assertFalse(CollectionUtil.isEmpty(Arrays.asList(1)));
    }

    @Test
    public void collectionUtilIsNotEmptyTest() {
        Assert.assertTrue(CollectionUtil.isNotEmpty(Arrays.asList(1)));
        Assert.assertFalse(CollectionUtil.isNotEmpty(Collections.emptyList()));
        Assert.assertFalse(CollectionUtil.isNotEmpty(null));
    }

    @Test
    public void collectionUtilFirstTest() {
        Assert.assertEquals(Integer.valueOf(1), CollectionUtil.first(Arrays.asList(1, 2, 3)));
        Assert.assertNull(CollectionUtil.first((List<?>) null));
        Assert.assertNull(CollectionUtil.first(Collections.emptyList()));
    }

    @Test
    public void collectionUtilLastTest() {
        Assert.assertEquals(Integer.valueOf(3), CollectionUtil.last(Arrays.asList(1, 2, 3)));
        Assert.assertNull(CollectionUtil.last((List<?>) null));
        Assert.assertNull(CollectionUtil.last(Collections.emptyList()));
    }

    @Test
    public void collectionUtilRemoveLastTest() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        CollectionUtil.removeLast(list);
        Assert.assertEquals(Arrays.asList(1, 2), list);
        CollectionUtil.removeLast(null);
        CollectionUtil.removeLast(Collections.emptyList());
    }

    @Test
    public void collectionUtilListTest() {
        List<Integer> list = CollectionUtil.list(1, 2, 3);
        Assert.assertEquals(3, list.size());
        Assert.assertTrue(CollectionUtil.list().isEmpty());
        Assert.assertTrue(CollectionUtil.list((Integer[]) null).isEmpty());
    }
}
