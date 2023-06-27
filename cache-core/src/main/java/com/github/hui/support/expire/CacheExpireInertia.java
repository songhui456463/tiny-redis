package com.github.hui.support.expire;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.hui.api.ICache;
import com.github.hui.api.ICacheExpire;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 惰性删除
 *
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireInertia<K, V> implements ICacheExpire<K, V> {

    /**
     * 单词清空的数量限制
     */
    private static final int LIMIT = 100;

    /**
     * 过期map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;


    public CacheExpireInertia(ICache<K, V> cache) {
        this.cache = cache;
    }

    /**
     * 执行过期操作
     *
     * @param key       key
     * @param expireAt  过期时间戳
     */
    private void expireKey(K key, Long expireAt) {
        // 删除逻辑
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            // 在从缓存中移除
            cache.remove(key);
        }
    }


    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)) {
            return;
        }

        // 惰性删除
        for (K key : keyList) {
            Long expireAt = this.expireMap.get(key);
            // 判断当前key是否需要删除
            if (expireAt == null) {
                return;
            }

            this.expireKey(key, expireAt);
        }
    }
}
