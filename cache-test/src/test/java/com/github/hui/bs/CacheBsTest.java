package com.github.hui.bs;

import com.github.hui.api.ICache;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBsTest {

    @Test
    public void cacheTest() {
        ICache<String, String> cache = CacheBs.<String, String>getInstance()
                .size(2)
                .build();
        cache.put("1", "2");
        cache.put("2", "2");
        cache.put("2", "4");
        cache.put("4", "4");
        System.out.println(cache.keySet());
        System.out.println(cache.values());
    }

    @Test
    public void lazyExpireTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String, String>getInstance()
                .size(3)
                .build();

        cache.put("1", "2");
        cache.put("2", "4");

        cache.expire("1", 40);
        Assert.assertEquals(2, cache.size());
        System.out.println(cache.get("1"));

        TimeUnit.MILLISECONDS.sleep(150);
        Assert.assertEquals(2, cache.size());
        System.out.println(cache.get("1"));
        Assert.assertEquals(1, cache.size());
    }

    @Test
    public void scheduleExpireTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String, String>getInstance()
                .size(3)
                .build();
        cache.put("1", "2");
        cache.put("2", "4");

        cache.expire("1", 40);
        Assert.assertEquals(2, cache.size());
        System.out.println(cache.get("1"));

        TimeUnit.SECONDS.sleep(2);
        Assert.assertEquals(1, cache.size());
        System.out.println(cache.get("1"));
    }
}
