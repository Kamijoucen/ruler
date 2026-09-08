package com.kamijoucen.ruler.test.option;

import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.spi.ImportMatchOrder;

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
