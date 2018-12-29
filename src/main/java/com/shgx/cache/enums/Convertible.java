package com.shgx.cache.enums;

public interface Convertible<T> {
    /**
     * 枚举转换成需要的类型
     * @return
     */
    T getValue();
}
