package sun.spring.redis.ext.exception;

/**
 * Created by root on 2015/10/28.
 */
public class CacheNotFoundException extends RuntimeException {
    public CacheNotFoundException(String message){
        super(message);
    }

    public CacheNotFoundException(Throwable e){
        super(e);
    }
}
