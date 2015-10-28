package sun.spring.redis.ext.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 2015/10/28.
 */
public class KeyValuePairsCacheBuilder extends AbstractCacheKeyBuilder {

    private Map<String, Object> map = new HashMap<String, Object>();

    public KeyValuePairsCacheBuilder put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    @Override
    public String getCacheKey() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append(":").append(value).append(";");
        }
        return sb.toString();
    }


}
