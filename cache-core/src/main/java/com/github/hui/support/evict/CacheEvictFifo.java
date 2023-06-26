package com.github.hui.support.evict;

import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEntry;
import com.github.hui.api.ICacheEvictContext;
import com.github.hui.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 先进先出剔除策略
 * @param <K>
 * @param <V>
 */
public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V> {

    /**
     * key按照时间put时间放入队列，先进先出
     */
    private final Queue<K> queue = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        CacheEntry<K, V> result = null;

        ICache<K, V> cache = context.cache();
        // cache.size() 当前缓存中元素的个数
        // context.size() 当前缓存的上限
        if (cache.size() >= context.size()) {
            // 超出容量限制，就需要剔除策略，剔除第一个
            K evictKey = queue.remove();

            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }

        // 添加key
        final K key = context.key();
        queue.add(key);

        return result;
    }
}
