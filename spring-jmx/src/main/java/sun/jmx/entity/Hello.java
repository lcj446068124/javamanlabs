package sun.jmx.entity;

/**
 * Created by 264929 on 2015/9/22.
 */
public class Hello extends AbstractHello {
    @Override
    public void before() {
        System.out.println("before");
    }

    @Override
    public void after() {
        System.out.println("after");
    }

    @Override
    public void execute() {
        System.out.println("exec");
    }

    public static void main(String[] args) {
        Hello hello = new Hello();

        hello.doWork();
    }
}
