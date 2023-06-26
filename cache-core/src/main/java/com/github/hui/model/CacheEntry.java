package com.github.hui.model;

import com.github.hui.api.ICacheEntry;

import java.awt.event.KeyEvent;

/**
 * 缓存具体信息，用于持久化
 * @param <K>
 * @param <V>
 */
public class CacheEntry<K, V> implements ICacheEntry<K,V> {

    /**
     * key
     */
    private final K key;

    /**
     * value
     */
    private final V value;

    /**
     * 新建元素
     * @param key
     * @param value
     * @param <K> 泛型
     * @param <V> 泛型
     * @return
     */
    public static <K, V> CacheEntry<K, V> of(
            final K key,
            final V value
    ) {
        return new CacheEntry<>(key, value);
    }

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
