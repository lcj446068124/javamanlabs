package sun.spring.scheduler.core;

import com.google.gson.Gson;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by sunyamorn on 3/6/16.
 */
public class ScheduleMonitor implements InitializingBean, DisposableBean {

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private List<TaskExecutor> taskExecutors = new LinkedList<>();

    private long period = 5000;  // unit: milliseconds

    private static final String MONITOR_REDIS_HASH_NAME = "SCH_MONITOR_HASH";

    private static final String THREAD_POOL_PREFIX = "THREAD_POOL";

    private static final String TASK_PREFIX = "TASK";

    private RedisTemplate<String, String> redisTemplate;

    public void setTaskExecutors(List<TaskExecutor> taskExecutors) {
        this.taskExecutors = taskExecutors;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
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

        // Reset monitor list
        cleanMonitorCache();

        executor.execute(new Runnable() {
            boolean flag = true;

            @Override
            public void run() {
                while (flag) {
                    try {
                        // thread pool monitor
                        if (taskExecutors != null && taskExecutors.size() > 0) {
                            for (TaskExecutor taskExecutor : taskExecutors) {
                                Map<String, String> map = null;
                                if (taskExecutor instanceof ThreadPoolTaskScheduler) {
                                    ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler) taskExecutor;
                                    map = infoOfJsonFormat(scheduler);
                                } else if (taskExecutor instanceof ThreadPoolTaskExecutor) {
                                    map = infoOfJsonFormat((ThreadPoolTaskExecutor) taskExecutor);
                                }
                                if (map != null && map.size() != 0) {
                                    putHashValue(map);
                                }
                            }
                        }
                        // task monitor
                        List<TaskStatusAware> taskStatusList = TaskRegistrationCenter.getTasks();
                        if (taskStatusList != null && taskStatusList.size() > 0) {
                            Map<String, String> map = null;
                            for (TaskStatusAware taskStatusAware : taskStatusList) {
                                TaskContext taskContext = taskStatusAware.getTaskContext();
                                map = infoOfJsonFormat(taskContext);
                                putHashValue(map);
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

    private Map<String, String> infoOfJsonFormat(ThreadPoolTaskScheduler scheduler) {
        Map<String, String> result = new HashMap<>();
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
        map.put("hostAddress", getLocalHostAddress());

        result.put(getKey(THREAD_POOL_PREFIX, scheduler.getThreadNamePrefix(), getLocalHostAddress()),
                new Gson().toJson(map));
        return result;
    }

    private Map<String, String> infoOfJsonFormat(ThreadPoolTaskExecutor executor) {
        Map<String, String> result = new HashMap<>();
        ThreadPoolExecutor threadPoolExecutor = executor.getThreadPoolExecutor();
        Map<String, Object> map = new HashMap<>();
        map.put("poolSize", threadPoolExecutor.getPoolSize());
        map.put("activeCount", threadPoolExecutor.getActiveCount());
        map.put("remainingCapacity", threadPoolExecutor.getQueue().remainingCapacity());
        map.put("blockQueueSize", threadPoolExecutor.getQueue().size());
        map.put("completedTaskCount", threadPoolExecutor.getCompletedTaskCount());
        map.put("corePoolSize", threadPoolExecutor.getCorePoolSize());
        map.put("maximumPoolSize", threadPoolExecutor.getMaximumPoolSize());
        map.put("taskCount", threadPoolExecutor.getTaskCount());
        map.put("isShutdown", threadPoolExecutor.isShutdown());
        map.put("threadNamePrefix", executor.getThreadNamePrefix());
        map.put("hostAddress", getLocalHostAddress());

        result.put(getKey(THREAD_POOL_PREFIX, executor.getThreadNamePrefix(), getLocalHostAddress()),
                new Gson().toJson(map));
        return result;
    }

    private Map<String, String> infoOfJsonFormat(TaskContext taskContext) {
        Map<String, String> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("lock", taskContext.getLock());
        map.put("scheduleName", taskContext.getScheduleName());
        map.put("status", taskContext.getStatus().getStatus());
        map.put("taskClassName", taskContext.getTaskClassName());
        map.put("taskName", taskContext.getTaskName());
        map.put("startTime", convertDate(taskContext.getStartTime()));
        map.put("endTime", convertDate(taskContext.getEndTime()));
        map.put("hostAddress", getLocalHostAddress());

        result.put(getKey(TASK_PREFIX, taskContext.getTaskName(), getLocalHostAddress()),
                new Gson().toJson(map));
        return result;
    }

    private void putHashValue(Map<String, String> map) {
        if (map != null && map.size() != 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                redisTemplate.opsForHash().put(MONITOR_REDIS_HASH_NAME, key, value);
            }
        }
    }

    private String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getKey(String prefix, String identity, String host) {
        return prefix + "_" + identity + "_" + host;
    }

    private String convertDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    private void cleanMonitorCache() {
        redisTemplate.delete(MONITOR_REDIS_HASH_NAME);
    }


}
