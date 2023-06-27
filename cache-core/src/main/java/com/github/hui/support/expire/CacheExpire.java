package com.github.hui.support.expire;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.hui.api.ICache;
import com.github.hui.api.ICacheExpire;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 普通轮询清理过期数据
 *
 * @param <K> key
 * @param <V> value
 */
public class CacheExpire<K, V> implements ICacheExpire<K, V> {

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

    /**
     * 线程池
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时任务
     */
    class ExpireThread implements Runnable {

        @Override
        public void run() {
            // 1 判断是否为空
            if (MapUtil.isEmpty(expireMap)) {
                return;
            }

            // 2 获取 key 进行处理
            int count = 0;
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                if (count >= LIMIT) {
                    return;
                }

                expireKey(entry.getKey(), entry.getValue());
                count++;
            }
        }
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
