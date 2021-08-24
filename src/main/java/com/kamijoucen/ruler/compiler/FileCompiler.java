package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.module.RulerFile;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.util.IOUtil;

public class FileCompiler {

    public static RulerFile compile(String content) {
        if (IOUtil.isBlank(content)) {
            return null;
        }
        RulerFile file = new RulerFile();
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(content);
        // 语法分析器
        Parser parser = new DefaultParser(lexical, file);
        // 开始解析语法
        parser.parse();
        return file;
    }

}
