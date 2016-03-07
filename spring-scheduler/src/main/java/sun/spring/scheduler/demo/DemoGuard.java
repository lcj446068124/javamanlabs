package sun.spring.scheduler.demo;

import sun.spring.scheduler.batch.AbstractGuard;

/**
 * Created by root on 2016/3/7.
 */
public class DemoGuard extends AbstractGuard {
    @Override
    public void lock() {
        System.out.println("Guard lock....");
    }

    @Override
    public void release() {
        System.out.println("Guard release....");
    }


}
