package sun.spring.scheduler.dao;

import sun.spring.scheduler.core.EntranceGuard;
import sun.spring.scheduler.core.ScheduleStatus;

import java.util.List;

/**
 * Created by yamorn on 2016/5/12.
 */
public interface DataAccessOperation<T, V> {

    T query(V id, boolean lock);

    List<T> queryAll();

    boolean insert(T entity);

    boolean updateLockStatus(V id, EntranceGuard lockStatus);

    boolean updateJobStatus(V id, ScheduleStatus scheduleStatus);

    boolean pessimisticLockQuery(final V id, OperationCallback<T, Boolean> callback);

    boolean releaseJobLock(V id);

    boolean hangup(V id, int flag);

    void log(T entity);


}
