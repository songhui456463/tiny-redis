package com.github.hui.api;

/**
 * 缓存实体
 * @param <K>
 * @param <V>
 */
public interface ICacheEntry<K, V> {

    /**
     * @return key
     */
    K key();

    /**
     * @return value
     */
    V value();
}
