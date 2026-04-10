package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.logic.parser.ArrayParser;
import com.kamijoucen.ruler.logic.parser.BlockParser;
import com.kamijoucen.ruler.logic.parser.BoolParser;
import com.kamijoucen.ruler.logic.parser.BreakParser;
import com.kamijoucen.ruler.logic.parser.ContinueParser;
import com.kamijoucen.ruler.logic.parser.ForEachParser;
import com.kamijoucen.ruler.logic.parser.FunParser;
import com.kamijoucen.ruler.logic.parser.IdentifierParser;
import com.kamijoucen.ruler.logic.parser.IfParser;
import com.kamijoucen.ruler.logic.parser.InfixParser;
import com.kamijoucen.ruler.logic.parser.NullParser;
import com.kamijoucen.ruler.logic.parser.NumberParser;
import com.kamijoucen.ruler.logic.parser.ParenParser;
import com.kamijoucen.ruler.logic.parser.ReturnParser;
import com.kamijoucen.ruler.logic.parser.RsonParser;
import com.kamijoucen.ruler.logic.parser.RuleParser;
import com.kamijoucen.ruler.logic.parser.StringParser;
import com.kamijoucen.ruler.logic.parser.TypeOfParser;
import com.kamijoucen.ruler.logic.parser.UnaryExpressionParser;
import com.kamijoucen.ruler.logic.parser.VarParser;
import com.kamijoucen.ruler.logic.parser.WhileParser;

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
