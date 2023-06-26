package com.github.hui.support.evict;

import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEntry;
import com.github.hui.api.ICacheEvictContext;
import com.github.hui.exception.CacheRuntimeException;

public class CacheEvictNone<K,V> extends AbstractCacheEvict<K, V>{

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {

        final ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()) {
            throw new CacheRuntimeException("已经满了，无法再添加元素");
        }
        return null;
    }
}
