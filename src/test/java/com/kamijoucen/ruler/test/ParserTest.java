package com.kamijoucen.ruler.test;

import com.kamijoucen.ruler.logic.parser.Parser;
import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.logic.lexer.Lexer;
import com.kamijoucen.ruler.logic.parser.Parsers;
import com.kamijoucen.ruler.types.parser.TokenStream;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.ClosureDefineNode;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.ast.ArrayNode;
import com.kamijoucen.ruler.types.ast.RsonNode;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    private ParseState createParser(String content) {
        return new ParseState(new TokenStream(Lexer.scan(content, null)));
    }

    @Test
    public void blockParserEmptyTest() {
        ParseState state = createParser("{}");
        BaseNode node = Parsers.BLOCK_PARSER.parse(state);
        Assert.assertTrue(node instanceof BlockNode);
        Assert.assertEquals(0, ((BlockNode) node).getBlocks().size());
    }

    @Test(expected = SyntaxException.class)
    public void blockParserMissingBraceTest() {
        ParseState state = createParser("{");
        Parsers.BLOCK_PARSER.parse(state);
    }

    @Test
    public void arrayParserEmptyTest() {
        ParseState state = createParser("[]");
        BaseNode node = Parsers.ARRAY_PARSER.parse(state);
        Assert.assertTrue(node instanceof ArrayNode);
        Assert.assertEquals(0, ((ArrayNode) node).getValues().size());
    }

    @Test
    public void rsonParserEmptyTest() {
        ParseState state = createParser("{}");
        BaseNode node = Parsers.RSON_PARSER.parse(state);
        Assert.assertTrue(node instanceof RsonNode);
        Assert.assertEquals(0, ((RsonNode) node).getProperties().size());
    }

    @Test
    public void funParserArrowTest() {
        ParseState state = createParser("fun(x) -> x + 1");
        BaseNode node = Parsers.FUN_PARSER.parse(state);
        Assert.assertTrue(node instanceof ClosureDefineNode);
        ClosureDefineNode closure = (ClosureDefineNode) node;
        Assert.assertNull(closure.getName());
        Assert.assertEquals(1, closure.getParam().size());
        Assert.assertTrue(closure.getBlock() instanceof BlockNode);
    }

    @Test
    public void funParserNamedBlockTest() {
        ParseState state = createParser("fun add(a, b) { return a + b; }");
        BaseNode node = Parsers.FUN_PARSER.parse(state);
        Assert.assertTrue(node instanceof ClosureDefineNode);
        ClosureDefineNode closure = (ClosureDefineNode) node;
        Assert.assertEquals("add", closure.getName());
        Assert.assertEquals(2, closure.getParam().size());
    }

    @Test(expected = SyntaxException.class)
    public void parseExpressionUnknownStartTest() {
        ParseState state = createParser(";");
        Parser.parseExpression(state);
    }

    @Test
    public void parseImportWithAliasTest() {
        ParseState state = createParser("import \"path/to/module\" alias;");
        ImportNode node = Parser.parseImport(state);
        Assert.assertEquals("path/to/module", node.getPath());
        Assert.assertEquals("alias", node.getAlias());
        Assert.assertFalse(node.isHasImportInfix());
    }

    @Test
    public void parseImportInfixTest() {
        ParseState state = createParser("import infix \"path/to/module\";");
        ImportNode node = Parser.parseImport(state);
        Assert.assertEquals("path/to/module", node.getPath());
        Assert.assertNull(node.getAlias());
        Assert.assertTrue(node.isHasImportInfix());
    }

    @Test(expected = SyntaxException.class)
    public void parseImportMissingAliasAndInfixTest() {
        ParseState state = createParser("import \"path/to/module\";");
        Parser.parseImport(state);
    }

    @Test
    public void precedenceChainedArithmeticTest() {
        ParseState state = createParser("1 + 2 * 3");
        BaseNode node = Parser.parseExpression(state);
        Assert.assertNotNull(node);
    }

    @Test
    public void precedenceMixedComparisonTest() {
        ParseState state = createParser("1 + 2 > 3 * 4");
        BaseNode node = Parser.parseExpression(state);
        Assert.assertNotNull(node);
    }

    @Test
    public void emptyStatementTest() {
        ParseState state = createParser("");
        Assert.assertEquals(TokenType.EOF, state.tokens.token().type);
    }
    @Test
    public void syntaxFailureRestoresGrammarContext() {
        ParseState state = createParser("while true { var x = ; }");
        try {
            Parser.parseStatement(state);
            Assert.fail("expected invalid variable expression to fail");
        } catch (SyntaxException expected) {
            Assert.assertTrue(state.root);
            Assert.assertFalse(state.inLoop);
        }
    }

    @Test
    public void independentParsesKeepSeparateTokenPositions() {
        ParseState first = createParser("1 + 2; 3;");
        ParseState second = createParser("4 * 5;");
        Parser.parseStatement(first);
        Assert.assertEquals("3", first.tokens.token().name);
        Parser.parseStatement(second);
        Assert.assertEquals(TokenType.EOF, second.tokens.token().type);
        Assert.assertEquals("3", first.tokens.token().name);
        Parser.parseStatement(first);
        Assert.assertEquals(TokenType.EOF, first.tokens.token().type);
    }
}
