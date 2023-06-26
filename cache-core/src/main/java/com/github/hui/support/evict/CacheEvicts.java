package com.github.hui.support.evict;

import com.github.hui.api.ICache;
import com.github.hui.api.ICacheEvict;

public final class CacheEvicts {

    private CacheEvicts() {}

    /**
     * 无策略
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ICacheEvict<K, V> none() {
        return new CacheEvictNone<>();
    }

    /**
     * 先进先出
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K,V> ICacheEvict<K, V> fifo() {
        return new CacheEvictFifo<K,V>();
    }


}
