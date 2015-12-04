package sun.jmx.entity;

/**
 * Created by 264929 on 2015/9/22.
 */
public abstract class AbstractHello {

    public abstract void before();

    public abstract void after();

    public abstract void execute();

    public void doWork(){
        before();
        execute();
        after();
    }
}
