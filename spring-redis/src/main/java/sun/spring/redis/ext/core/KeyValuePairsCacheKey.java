package sun.spring.redis.ext.core;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2015/10/29.
 */
public class KeyValuePairsCacheKey extends AbstractCacheKey<Map<String, Object>> {

    private Map<String, Object> map = new HashMap<String, Object>();

    public KeyValuePairsCacheKey(String cacheKey) {
        super(cacheKey);
    }

    public KeyValuePairsCacheKey() {
    }

    public CacheKey<Map<String, Object>> put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    @Override
    public String getCacheKey() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                sb.append(key)
                        .append(CacheKeyGenerator.KEY_VALUE_SEPARATOR)
                        .append(value).append(CacheKeyGenerator.KEY_VALUE_PAIRS_SEPARATOR);
            }
            // Maintain the inner cacheKey value which is transferred to parseCacheKey() for parsing.
            this.cacheKey = sb.toString();
            return sb.toString();
    }

    @Override
    public Map<String, Object> parseCacheKey() {
        if (!StringUtils.isEmpty(this.cacheKey)) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String pair : cacheKey.split(CacheKeyGenerator.KEY_VALUE_PAIRS_SEPARATOR)) {
                String[] array = pair.split(CacheKeyGenerator.KEY_VALUE_SEPARATOR);
                map.put(array[0], array[1]);
            }
            return map;
        }
        return null;
    }
}
