package com.github.hui.core;


import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEvict;
import com.github.hui.api.ICacheEvictContext;
import com.github.hui.exception.CacheRuntimeException;
import com.github.hui.support.evict.CacheEvictContext;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 缓存的实现类
 * @param <K> key
 * @param <V> value
 */
public class Cache<K, V> implements ICache<K, V> {

    /**
     * 缓存信息
     */
    private Map<K, V> map;

    /**
     * 大小限制，超出触发驱除策略
     */
    private int sizeLimit;

    /**
     * 剔除策略
     */
    private ICacheEvict<K, V> evict;

    /**
     * 设置缓存实现
     * @param map 具体的map实现
     * @return
     */
    public Cache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置缓存上限
     * @param sizeLimit 上限大小
     * @return
     */
    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    public Cache<K, V> cacheEvict(ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        // todo 刷新策略
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        // todo
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        // 前置准备，设置上下文信息(包括最大容量，需增加的key，当前缓存信息)
        context.size(sizeLimit)
                .key(key)
                .cache(this);

        // 1 将上下文信息传给对应的操作类
        evict.evict(context);

        // 2 判断驱除后容量是否充足
        if (isSizeLimit()) {
            throw new CacheRuntimeException("达到最大限制");
        }

        // 3 执行添加
        return map.put(key, value);
    }

    /**
     * 判断大小是否达到最大限制
     * @return
     */
    private boolean isSizeLimit() {
        final int currentSize = this.size();
        return currentSize >= sizeLimit;
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
