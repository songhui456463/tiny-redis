package com.github.hui.support.expire;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.github.hui.api.ICache;
import com.github.hui.api.ICacheExpire;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 轮询清理过期数据
 * 按照过期时间去清理过期数据
 * 遇到未过期的数据就可以停止任务
 * 相当于以空间换时间
 *
 * @param <K> key
 * @param <V> value
 */
public class CacheExpireOnSchedule<K, V> implements ICacheExpire<K, V> {

    /**
     * 单词清空的数量限制
     */
    private static final int LIMIT = 100;

    /**
     * 排序缓存存储
     * <p>
     * 使用按照时间排序的缓存处理。
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>((o1, o2) -> (int) (o1 - o2));

    /**
     * 过期缓存
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

    public CacheExpireOnSchedule(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 执行定时任务
     */
    private class ExpireThread implements Runnable {

        @Override
        public void run() {
            // 1 判断是否为空
            if (MapUtil.isEmpty(sortMap)) {
                return;
            }

            // 2 获取 key 进行处理
            int count = 0;
            clean:
            for (Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
                final Long expireAt = entry.getKey();
                List<K> expireKeys = entry.getValue();

                // 判断队列是否为空
                if (CollectionUtil.isEmpty(expireKeys)) {
                    sortMap.remove(expireAt);
                    continue;
                }

                // 执行删除逻辑，遍历expireKeys
                long currentTime = System.currentTimeMillis();
                if (currentTime >= expireAt) {
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()) {
                        K key = iterator.next();
                        // 先删除自己
                        iterator.remove();
                        cache.remove(key);
                        if (++count >= LIMIT) {
                            break clean;
                        }
                    }
                    sortMap.remove(expireAt);
                } else {
                    // 直接跳过，没有过期的信息
                    return;
                }
            }
        }
    }

    @Override
    public void expire(K key, long expireAt) {
        List<K> keys = sortMap.get(expireAt);
        if (CollectionUtil.isEmpty(keys)) {
            keys = new ArrayList<>();
        }
        keys.add(key);

        // 设置对应的信息
        sortMap.put(expireAt, keys);
        expireMap.put(key, expireAt);
    }

    public void expireKey(K key, Long expireAt) {
        // 删除逻辑
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            expireMap.remove(key);
            // 在从缓存中移除
            cache.remove(key);
            sortMap.remove(key);
        }
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)) {
            return;
        }

        for (K key : keyList) {
            Long expireAt = expireMap.get(key);
            if (expireAt == null) {
                return;
            }
            this.expireKey(key, expireAt);
        }
    }
}
