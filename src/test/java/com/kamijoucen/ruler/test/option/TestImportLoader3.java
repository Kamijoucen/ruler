package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.config.option.CustomImportLoader;
import com.kamijoucen.ruler.config.option.ImportMatchOrder;

@ImportMatchOrder(order = 103)
public class TestImportLoader3 implements CustomImportLoader {

    @Override
    public String load(String path) {
        return "3";
    }

    @Override
    public boolean match(String path) {
        // TODO Auto-generated method stub
        return false;
    }
}
