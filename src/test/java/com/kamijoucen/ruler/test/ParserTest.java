package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.component.DefaultLexical;
import com.kamijoucen.ruler.component.Parsers;
import com.kamijoucen.ruler.component.TokenStreamImpl;
import com.kamijoucen.ruler.application.impl.RulerConfigurationImpl;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.BlockNode;
import com.kamijoucen.ruler.domain.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.domain.ast.factor.ArrayNode;
import com.kamijoucen.ruler.domain.ast.factor.RsonNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private RulerConfigurationImpl configuration;

    @Before
    public void init() {
        configuration = new RulerConfigurationImpl();
    }

    private AtomParserManager createParser(String content) {
        DefaultLexical lexical = new DefaultLexical(content, null, configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();
        return new AtomParserManager(tokenStream, configuration);
    }

    @Test
    public void blockParserEmptyTest() {
        AtomParserManager manager = createParser("{}");
        BaseNode node = Parsers.BLOCK_PARSER.parse(manager);
        Assert.assertTrue(node instanceof BlockNode);
        Assert.assertEquals(0, ((BlockNode) node).getBlocks().size());
    }

    @Test(expected = SyntaxException.class)
    public void blockParserMissingBraceTest() {
        AtomParserManager manager = createParser("{");
        Parsers.BLOCK_PARSER.parse(manager);
    }

    @Test
    public void arrayParserEmptyTest() {
        AtomParserManager manager = createParser("[]");
        BaseNode node = Parsers.ARRAY_PARSER.parse(manager);
        Assert.assertTrue(node instanceof ArrayNode);
        Assert.assertEquals(0, ((ArrayNode) node).getValues().size());
    }

    @Test
    public void rsonParserEmptyTest() {
        AtomParserManager manager = createParser("{}");
        BaseNode node = Parsers.RSON_PARSER.parse(manager);
        Assert.assertTrue(node instanceof RsonNode);
        Assert.assertEquals(0, ((RsonNode) node).getProperties().size());
    }

    @Test
    public void funParserArrowTest() {
        AtomParserManager manager = createParser("fun(x) -> x + 1");
        BaseNode node = Parsers.FUN_PARSER.parse(manager);
        Assert.assertTrue(node instanceof ClosureDefineNode);
        ClosureDefineNode closure = (ClosureDefineNode) node;
        Assert.assertNull(closure.getName());
        Assert.assertEquals(1, closure.getParam().size());
        Assert.assertTrue(closure.getBlock() instanceof BlockNode);
    }

    @Test
    public void funParserNamedBlockTest() {
        AtomParserManager manager = createParser("fun add(a, b) { return a + b; }");
        BaseNode node = Parsers.FUN_PARSER.parse(manager);
        Assert.assertTrue(node instanceof ClosureDefineNode);
        ClosureDefineNode closure = (ClosureDefineNode) node;
        Assert.assertEquals("add", closure.getName());
        Assert.assertEquals(2, closure.getParam().size());
    }

    @Test(expected = SyntaxException.class)
    public void parseExpressionUnknownStartTest() {
        AtomParserManager manager = createParser(";");
        manager.parseExpression();
    }

    @Test
    public void parseImportWithAliasTest() {
        AtomParserManager manager = createParser("import \"path/to/module\" alias;");
        ImportNode node = manager.parseImport();
        Assert.assertEquals("path/to/module", node.getPath());
        Assert.assertEquals("alias", node.getAlias());
        Assert.assertFalse(node.isHasImportInfix());
    }

    @Test
    public void parseImportInfixTest() {
        AtomParserManager manager = createParser("import infix \"path/to/module\";");
        ImportNode node = manager.parseImport();
        Assert.assertEquals("path/to/module", node.getPath());
        Assert.assertNull(node.getAlias());
        Assert.assertTrue(node.isHasImportInfix());
    }

    @Test(expected = SyntaxException.class)
    public void parseImportMissingAliasAndInfixTest() {
        AtomParserManager manager = createParser("import \"path/to/module\";");
        manager.parseImport();
    }

    @Test
    public void precedenceChainedArithmeticTest() {
        AtomParserManager manager = createParser("1 + 2 * 3");
        BaseNode node = manager.parseExpression();
        Assert.assertNotNull(node);
    }

    @Test
    public void precedenceMixedComparisonTest() {
        AtomParserManager manager = createParser("1 + 2 > 3 * 4");
        BaseNode node = manager.parseExpression();
        Assert.assertNotNull(node);
    }

    @Test
    public void emptyStatementTest() {
        AtomParserManager manager = createParser("");
        Assert.assertEquals(TokenType.EOF, manager.getTokenStream().token().type);
    }
}
