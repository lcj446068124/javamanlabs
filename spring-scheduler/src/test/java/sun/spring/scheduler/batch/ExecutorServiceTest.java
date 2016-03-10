package sun.spring.scheduler.batch;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by root on 2016/3/10.
 */
public class ExecutorServiceTest {
    @Test
    public void testThreadPoolWaitAllTaskComplete() throws ExecutionException, InterruptedException {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.initialize();

        Stack<Future> stack = new Stack<>();

        Future future1 = threadPoolTaskScheduler.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("before task");
                Thread.sleep(5000);
                System.out.println("after task");
                return 10;
            }
        });
        Future future2 = threadPoolTaskScheduler.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("before task2");
                Thread.sleep(5000);
                System.out.println("after task2");
                return 20;
            }
        });
        stack.push(future1);
        stack.push(future2);
        
        while (!stack.empty()) {
            Future future = stack.pop();
            System.out.println(future.get() +" finish");
        }
//        System.out.println("main t");
//        Object obj = future.get();
//        System.out.println(obj);
        System.out.println("done");


    }
}
