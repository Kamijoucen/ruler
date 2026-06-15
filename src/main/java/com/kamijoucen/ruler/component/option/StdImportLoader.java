package com.kamijoucen.ruler.component.option;

import com.kamijoucen.ruler.logic.util.IOUtil;

@ImportMatchOrder(order = Integer.MAX_VALUE)
public class StdImportLoader implements CustomImportLoader {

    @Override
    public String load(String path) {
        String resourcePath = path.startsWith("/") ? path : "/" + path;
        return IOUtil.read(StdImportLoader.class.getResourceAsStream(resourcePath));
    }

    @Override
    public boolean match(String path) {
        if (IOUtil.isBlank(path)) {
            return false;
        }
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }

}
