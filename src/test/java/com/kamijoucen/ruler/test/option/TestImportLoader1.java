package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.config.option.CustomImportLoader;
import com.kamijoucen.ruler.config.option.ImportMatchOrder;

@ImportMatchOrder(order = 101)
public class TestImportLoader1 implements CustomImportLoader {

    @Override
    public String load(String path) {
        return "1";
    }

    @Override
    public boolean match(String path) {
        // TODO Auto-generated method stub
        return false;
    }
}
