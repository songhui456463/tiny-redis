package com.github.hui.bs;

import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEvict;
import com.github.hui.core.Cache;
import com.github.hui.support.evict.CacheEvicts;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存引导类(构建缓存)
 */
public final class CacheBs<K, V> {

    private CacheBs() {
    }

    /**
     * 创建对象实例
     *
     * @param <K> key
     * @param <V> value
     * @return
     */
    public static <K, V> CacheBs<K, V> getInstance() {
        return new CacheBs<>();
    }

    /**
     * map的默认实现
     */
    private Map<K, V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;


    /**
     * 剔除策略,默认按照key先进先出
     */
    private ICacheEvict<K, V> evict = CacheEvicts.fifo();

    /**
     * 设置map的实现
     *
     * @param map
     * @return
     */
    public CacheBs<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置map的上限
     *
     * @param size
     * @return
     */
    public CacheBs<K, V> size(int size) {
        this.size = size;
        return this;
    }

    /**
     *
     *
     * @return
     */
    public CacheBs<K, V> evict(ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    /**
     * 构建缓存信息
     * @return
     */
    public ICache<K, V> build() {
        Cache<K, V> cache = new Cache<>();
        return cache.map(map)
                .sizeLimit(size)
                .cacheEvict(evict);
    }
}
