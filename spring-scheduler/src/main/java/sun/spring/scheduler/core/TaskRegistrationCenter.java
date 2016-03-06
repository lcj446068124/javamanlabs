package sun.spring.scheduler.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by sunyamorn on 3/7/16.
 */
public class TaskRegistrationCenter {

    private static Map<String, TaskStatusAware> map = new HashMap<>();

    public static synchronized void register(TaskStatusAware taskStatusAware) {
        if (taskStatusAware != null) {
            map.put(taskStatusAware.getTaskContext().getTaskName(), taskStatusAware);
        }
    }

    public static synchronized TaskStatusAware getTask(String taskName) {
        return map.get(taskName);
    }

    public static List<TaskStatusAware> getTasks() {
        List<TaskStatusAware> list = new LinkedList<>();
        for (Map.Entry<String, TaskStatusAware> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }
}
