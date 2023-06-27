package com.github.hui.core;


import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEvict;
import com.github.hui.api.ICacheEvictContext;
import com.github.hui.api.ICacheExpire;
import com.github.hui.exception.CacheRuntimeException;
import com.github.hui.support.evict.CacheEvictContext;
import com.github.hui.support.expire.CacheExpire;

import java.util.Collection;
import java.util.Collections;
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
     * 过期策略
     */
    private ICacheExpire<K, V> cacheExpire;

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

    /**
     * 设置剔除策略
     * @param evict 剔除策略
     * @return
     */
    public Cache<K, V> cacheEvict(ICacheEvict<K, V> evict) {
        this.evict = evict;
        return this;
    }

    /**
     * 初始化
     */
    public void init() {
        this.cacheExpire = new CacheExpire<>(this);
    }

    /**
     * 设置过期策略
     * @param cacheExpire 设置过期策略
     * @return
     */
    public Cache<K, V> cacheExpire(ICacheExpire<K, V> cacheExpire) {
        this.cacheExpire = cacheExpire;
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

    /**
     *  设置多少时间后过期
     * @param key           key
     * @param timeInMills   毫秒时间
     * @return
     */
    @Override
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;
        this.expireAt(key, expireTime);
        return this;
    }

    /**
     * 指定过期时间戳
     * @param key          key
     * @param timeInMills  时间戳
     * @return
     */
    @Override
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.cacheExpire.expire(key, timeInMills);
        return this;
    }

    @Override
    public V get(Object key) {
        // 惰性删除
        K refreshKey = (K) key;
        this.cacheExpire.refreshExpire(Collections.singletonList(refreshKey));
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
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
