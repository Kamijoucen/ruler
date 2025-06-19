package com.kamijoucen.ruler.util;

import java.util.*;

/**
 * 集合工具类，提供常用的集合操作方法
 *
 * @author Kamijoucen
 */
public class CollectionUtil {

    /**
     * 私有构造函数，防止实例化
     */
    private CollectionUtil() {
        throw new AssertionError("No instances of CollectionUtil");
    }

    /**
     * 检查Map是否为空
     *
     * @param map 要检查的Map
     * @return 如果map为null或空返回true
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查集合是否为空
     *
     * @param collection 要检查的集合
     * @return 如果collection为null或空返回true
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 检查集合是否非空
     *
     * @param collection 要检查的集合
     * @return 如果collection不为null且不为空返回true
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 获取列表的第一个元素
     *
     * @param <T> 元素类型
     * @param list 列表
     * @return 第一个元素，如果列表为空返回null
     */
    public static <T> T first(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取列表的最后一个元素
     *
     * @param <T> 元素类型
     * @param list 列表
     * @return 最后一个元素，如果列表为空返回null
     */
    public static <T> T last(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 移除列表的最后一个元素
     *
     * @param list 要操作的列表
     */
    public static void removeLast(List<?> list) {
        if (isNotEmpty(list)) {
            list.remove(list.size() - 1);
        }
    }

    /**
     * 创建包含指定元素的ArrayList
     *
     * @param <T> 元素类型
     * @param values 要添加到列表的元素
     * @return 包含指定元素的新ArrayList
     */
    @SafeVarargs
    public static <T> List<T> list(T... values) {
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     * 创建包含指定元素的不可修改列表
     *
     * @param <T> 元素类型
     * @param values 要添加到列表的元素
     * @return 包含指定元素的不可修改列表
     */
    @SafeVarargs
    public static <T> List<T> immutableList(T... values) {
        if (values == null || values.length == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(Arrays.asList(values));
    }
}
