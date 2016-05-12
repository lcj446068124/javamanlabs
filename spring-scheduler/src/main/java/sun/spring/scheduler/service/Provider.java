package sun.spring.scheduler.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yamorn on 2016/5/12.
 */
public class Provider {

    private static final Provider provider = new Provider();

    private static Map<String, Services> cache = new ConcurrentHashMap<>();

    private Provider() {
    }

    public static Provider getInstance() {
        return provider;
    }

    public void register(String key, Services services) {
        cache.put(key, services);
    }

    public Services get(String key) {
        return cache.get(key);
    }
}
