package sun.spring.redis.ext.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by root on 2015/10/28.
 */
public abstract class AbstractBaseCache<V, T> implements ICache<V, T>, InitializingBean, DisposableBean{
    private Context context;

    public Context getContext(){
        return this.context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        context = new Context(this);
    }

    @Override
    public void destroy() throws Exception {

    }
}
