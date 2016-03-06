package sun.spring.scheduler.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunyamorn on 3/6/16.
 */
public class ScheduleMonitor implements InitializingBean, DisposableBean {

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private List<ThreadPoolTaskScheduler> schedulers = new LinkedList<>();

    private long period = 5000;  // unit: milliseconds

    public void setSchedulers(List<ThreadPoolTaskScheduler> schedulers) {
        this.schedulers = schedulers;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public void destroy() throws Exception {
        if (executor != null) {
            executor.shutdown();
            // wait for one seconds
            if (executor.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert (executor != null);

        executor.execute(new Runnable() {
            boolean flag = true;

            @Override
            public void run() {
                while (flag) {
                    try {
                        if (schedulers != null && schedulers.size() > 0) {
                            for (ThreadPoolTaskScheduler scheduler : schedulers) {
                                String json = infoOfJsonFormat(scheduler);
                                System.out.println(json);

                                List<TaskStatusAware> tasks = TaskRegistrationCenter.getTasks();
                                for(TaskStatusAware taskStatus : tasks){
                                    TaskContext taskContext = taskStatus.getTaskContext();
                                    String taskName = taskContext.getTaskName();
                                    String status = taskContext.getStatus().getStatus();
                                    int lock = taskContext.getLock();
                                    Date startTime = taskContext.getStartTime();
                                    Date endTime = taskContext.getEndTime();
                                    System.out.println(taskName + " = " + status );

                                }
                            }
                        }
                    } finally {
                        // Infinite Loop
                        try {
                            Thread.sleep(period);
                        } catch (InterruptedException e) {
                            flag = false;
                        }
                    }
                }
            }
        });
    }

    private String infoOfJsonFormat(ThreadPoolTaskScheduler scheduler) {
        ScheduledThreadPoolExecutor stpe = scheduler.getScheduledThreadPoolExecutor();
        Map<String, Object> map = new HashMap<>();
        map.put("poolSize", scheduler.getPoolSize());
        map.put("activeCount", scheduler.getActiveCount());
        map.put("remainingCapacity", stpe.getQueue().remainingCapacity());
        map.put("blockQueueSize", stpe.getQueue().size());
        map.put("completedTaskCount", stpe.getCompletedTaskCount());
        map.put("corePoolSize", stpe.getCorePoolSize());
        map.put("maximumPoolSize", stpe.getMaximumPoolSize());
        map.put("taskCount", stpe.getTaskCount());
        map.put("isShutdown", stpe.isShutdown());
        map.put("threadNamePrefix", scheduler.getThreadNamePrefix());
        try {
            map.put("hostAddress", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            // ignore
        }

        Gson gson = new Gson();
        return gson.toJson(map);
    }


}
