package tests;

import com.kamijoucen.ruler.RuleScript;
import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void test3() {
        String str = "(a + b) * c / c";

        Lexical lexical = new DefaultLexical(str);

        lexical.nextToken();

        DefaultParser parser = new DefaultParser(lexical);

        BaseAST baseAST = parser.parseExpression();

        System.out.println(baseAST);

    }

    @Test
    public void test4() {

        String str = "a -- a --(-a-+b+-c)";

        Lexical lexical = new DefaultLexical(str);

        lexical.nextToken();

        DefaultParser parser = new DefaultParser(lexical);

        BaseAST baseAST = parser.parseExpression();

        System.out.println(baseAST);

    }


    @Test
    public void test5() {

        String str = "1.158 - 3.559;";

        Lexical lexical = new DefaultLexical(str);

        DefaultParser parser = new DefaultParser(lexical);

        List<BaseAST> parse = parser.parse();

        for (BaseAST baseAST : parse) {
            BaseValue eval = baseAST.eval(new DefaultScope(null));

            System.out.println(eval.toString());
        }

    }


    @Test
    public void test6() {

        String str = "a = 0; if false { a = 1; b = 66;} else if true {a=2;} else { a = 3;}";

        Lexical lexical = new DefaultLexical(str);

        DefaultParser parser = new DefaultParser(lexical);

        List<BaseAST> parse = parser.parse();

        DefaultScope scope = new DefaultScope(null);
        for (BaseAST baseAST : parse) {
            BaseValue eval = baseAST.eval(scope);
            System.out.println(eval.toString());
        }

        System.out.println(scope);

    }

    @Test
    public void test7() {

        String str = "a = 0; if false { a = 1; b = 66;} else if true {a=2;} else { a = 3;}";

        RuleScript runner = Ruler.compile(str);

        runner.run();

    }


    @Test
    public void test8() {

        String str = "str = ''; if 5 < 5 { str = '123123'; } else { str = makeItPossible();} println(str, 1, 2, 3, '李思岑');";

        String str2 = "text = \"hello world!\"; println(text);";

        RuleScript runner = Ruler.compile(str);

        runner.run();

    }

    @Test
    public void test9() {

        String str2 = "i = 0; while i <= 5 { println(i); if i == 3 { break; } i = i + 1;  }";

        RuleScript runner = Ruler.compile(str2);

        runner.run();

    }

    @Test
    public void test10() {

        Integer[] param = new Integer[] {1};

        Integer[] funcParam = Arrays.copyOfRange(param, 1, param.length);

        System.out.println(Arrays.toString(funcParam));


    }

}
