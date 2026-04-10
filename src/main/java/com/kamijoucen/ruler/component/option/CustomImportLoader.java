package com.kamijoucen.ruler.component.option;

@ImportMatchOrder
public interface CustomImportLoader {

    boolean match(String path);

    String load(String path);

}
