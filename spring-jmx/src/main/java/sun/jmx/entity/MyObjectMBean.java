package sun.jmx.entity;

/**
 * Created by 264929 on 2015/9/22.
 */
public interface MyObjectMBean {
    public long getId();

    public void setId(long id);

    public String getName();

    public void setName(String name);

    public String show();
}
