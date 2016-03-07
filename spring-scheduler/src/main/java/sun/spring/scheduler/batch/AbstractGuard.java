package sun.spring.scheduler.batch;

/**
 * Created by root on 2016/3/7.
 */
public abstract class AbstractGuard implements Guard {

    private String name;

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
