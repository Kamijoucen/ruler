package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.config.option.CustomImportLoader;
import com.kamijoucen.ruler.config.option.ImportMatchOrder;

@ImportMatchOrder(order = 102)
public class TestImportLoader2 implements CustomImportLoader {

    @Override
    public String load(String path) {
        return "2";
    }

    @Override
    public boolean match(String path) {
        // TODO Auto-generated method stub
        return false;
    }
}
