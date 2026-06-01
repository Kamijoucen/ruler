package com.kamijoucen.ruler.logic.parser;

public class Parsers {

    public static final ArrayParser ARRAY_PARSER = new ArrayParser();
    public static final BlockParser BLOCK_PARSER = new BlockParser();
    public static final BoolParser BOOL_PARSER = new BoolParser();
    public static final BreakParser BREAK_PARSER = new BreakParser();
    public static final ContinueParser CONTINUE_PARSER = new ContinueParser();
    public static final ForEachParser FOR_EACH_PARSER = new ForEachParser();
    public static final FunParser FUN_PARSER = new FunParser();
    public static final IdentifierParser IDENTIFIER_PARSER = new IdentifierParser();
    public static final IfParser IF_PARSER = new IfParser();
    public static final InfixParser INFIX_PARSER = new InfixParser();
    public static final MatchParser MATCH_PARSER = new MatchParser();
    public static final NullParser NULL_PARSER = new NullParser();
    public static final NumberParser NUMBER_PARSER = new NumberParser();
    public static final ParenParser PAREN_PARSER = new ParenParser();
    public static final ReturnParser RETURN_PARSER = new ReturnParser();
    public static final RsonParser RSON_PARSER = new RsonParser();
    public static final RuleParser RULE_PARSER = new RuleParser();
    public static final StringParser STRING_PARSER = new StringParser();
    public static final TypeOfParser TYPE_OF_PARSER = new TypeOfParser();
    public static final UnaryExpressionParser UNARY_EXPRESSION_PARSER = new UnaryExpressionParser();
    public static final VarParser VAR_PARSER = new VarParser();
    public static final WhileParser WHILE_PARSER = new WhileParser();

}
