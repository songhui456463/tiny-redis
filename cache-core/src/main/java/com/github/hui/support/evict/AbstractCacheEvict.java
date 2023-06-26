package com.github.hui.support.evict;

import com.github.hui.api.ICacheEntry;
import com.github.hui.api.ICacheEvict;
import com.github.hui.api.ICacheEvictContext;

public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V>  context) {
        return doEvict(context);
    }

    /**
     * 驱除策略的实现
     * @param context
     * @return
     */
    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V>  context);
}
