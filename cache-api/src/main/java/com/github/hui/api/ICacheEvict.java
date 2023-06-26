package com.github.hui.api;

public interface ICacheEvict<K, V> {


    /**
     * 驱除策略
     *
     * @param context 上下文
     * @return 被移除的明细，没有时返回 null
     */
    ICacheEntry<K, V> evict(final ICacheEvictContext<K, V> context);
}
