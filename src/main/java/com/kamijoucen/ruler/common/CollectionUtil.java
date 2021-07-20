package com.kamijoucen.ruler.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null) {
            return true;
        }
        return collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T> T first(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public static <T> T last(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }


    public static void removeLast(List<?> list) {
        if (isNotEmpty(list)) {
            list.remove(list.size() - 1);
        }
    }

    public static <T> List<T> list(T... values) {
        if (values == null || values.length == 0) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(Arrays.asList(values));
    }

}
