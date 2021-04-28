package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class DefaultParser implements Parser {

    private Lexical lexical;

    private List<BaseAST> astList;

    public DefaultParser(Lexical lexical) {
        this.lexical = lexical;
        this.astList = new ArrayList<BaseAST>();
    }

    @Override
    public List<BaseAST> parse() {

        lexical.nextToken();

        while (lexical.getToken().type != TokenType.EOF) {



        }


        return astList;
    }



}
