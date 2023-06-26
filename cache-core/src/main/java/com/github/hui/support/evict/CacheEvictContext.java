package com.github.hui.support.evict;

import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEvictContext;
import com.github.hui.core.CacheContext;

/**
 * 上下文环境，给具体操作类提供信息
 * @param <K>
 * @param <V>
 */
public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {

    /**
     *
     */
    private K key;

    /**
     * 缓存
     */
    private ICache<K, V> cache;

    /**
     * 最大限制
     */
    private int size;

    /**
     * 保存key信息
     * @param key
     * @return
     */
    public CacheEvictContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    /**
     * 保存缓存信息
     * @param cache
     * @return
     */
    public CacheEvictContext<K, V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    /**
     * 保存最大容量信息
     * @return
     */
    public CacheEvictContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    @Override
    public int size() {
        return size;
    }
}
