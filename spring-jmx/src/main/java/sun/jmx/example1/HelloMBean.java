package sun.jmx.example1;

/**
 * Created by root on 2016/1/13.
 */
public interface HelloMBean {

    void sayHello();

    int add(int x, int y);

    String getName();

    int getCacheSize();

    void setCacheSize(int size);
}
