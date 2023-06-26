package com.github.hui.bs;

import com.github.hui.api.ICache;
import org.junit.Test;

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
}
