package sun.spring.redis.ext.exception;

/**
 * Created by root on 2015/10/28.
 */
public class CacheDumplicateException extends RuntimeException{

    public CacheDumplicateException(String message) {
        super(message);
    }

    public CacheDumplicateException(Throwable e){
        super(e);
    }
}
