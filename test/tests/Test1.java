package tests;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import org.junit.jupiter.api.Test;

public class Test1 {

    @Test
    public void test1() {
        String str = "n $_n 3.5 7  = == - + ** 1.1  /   '55' 1.f  /**===()-- 1. ";

        String str1 = " '11'  --";

        Lexical lexical = new DefaultLexical(str);

        Token token = lexical.nextToken();
        while (token.type != TokenType.EOF) {
            System.out.println(token);
            token = lexical.nextToken();
        }
    }

    @Test
    public void test2() {
        String str = "$log(name, $size)";

        Lexical lexical = new DefaultLexical(str);

        lexical.nextToken();

        DefaultParser parser = new DefaultParser(lexical);

        BaseAST baseAST = parser.parseExpression();

        System.out.println(baseAST);

    }

}
