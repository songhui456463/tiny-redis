package com.github.hui.api;

public interface ICacheEvictContext<K, V> {

    /**
     * 新增key
     * @return
     */
    K key();

    /**
     * 缓存实现类
     * @return
     */
    ICache<K, V> cache();

    /**
     * 获取缓存上限大小
     * @return
     */
    int size();
}
