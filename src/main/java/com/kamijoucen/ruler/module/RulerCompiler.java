package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.util.IOUtil;

public class RulerCompiler {

    public static RulerFile compile(String content) {
        if (IOUtil.isBlank(content)) {
            return null;
        }
        RulerFile file = new RulerFile();

        Parser parser = new DefaultParser(new DefaultLexical(content), file);
        parser.parse();

        return file;
    }

}
