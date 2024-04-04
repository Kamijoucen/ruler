package com.kamijoucen.ruler.config.option;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.util.IOUtil;

public class StdImportLoadImpl implements CustomImportLoad {

    @Override
    public String load(String path) {
        return IOUtil.read(Ruler.class.getResourceAsStream(path));
    }

    @Override
    public boolean match(String path) {
        if (IOUtil.isBlank(path)) {
            return false;
        }
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }

}
