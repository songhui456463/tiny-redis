package com.github.hui.api;

import java.util.Collection;

public interface ICacheExpire<K, V> {

    /**
     * 按指定时间过期
     *
     * @param key       key
     * @param expireAt  过期时间戳
     */
    void expire(final K key, final  long expireAt);

    /**
     * 惰性删除中需要处理的keys
     *
     * @param keyList  keys
     */
    void refreshExpire(final Collection<K> keyList);
}
