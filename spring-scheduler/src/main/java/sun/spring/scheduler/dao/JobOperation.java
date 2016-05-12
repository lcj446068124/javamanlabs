package sun.spring.scheduler.dao;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.spring.scheduler.core.EntranceGuard;
import sun.spring.scheduler.core.ScheduleStatus;
import sun.spring.scheduler.domain.JobEntity;

import java.util.Date;

/**
 * Created by yamorn on 2016/5/12.
 */
public class JobOperation extends AbstractDataAccessOperation<JobEntity, String> {

    public static final String TABLE_JOB_CONTROL = "JOB_CONTROL";

    private static final String[] controlFields = new String[]{
            "JOB_NAME",
            "JOB_STATUS",
            "JOB_LOCK",
            "RUN_FLAG",
            "JOB_GROUP",
            "JOB_CLASS_NAME",
            "SCHEDULE_NAME",
            "LAST_START_TIME",
            "LAST_END_TIME"
    };

    private String tableName() {
        return this.tablePrefix + "_" + TABLE_JOB_CONTROL;
    }

    @Override
    public JobEntity query(String id, boolean lock) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ? " + (lock ? "FOR UPDATE" : ""),
                arrayJoin(controlFields), tableName(), controlFields[0]);
        return jdbcTemplate.queryForObject(sql, JobEntity.class);
    }

    @Override
    public boolean insert(JobEntity entity) {
        final String sql = String.format("INSERT INTO %s (%s) VALUES (?,?,?,?,?,?,?,?,?)", tableName(), arrayJoin(controlFields));
        final Object[] args = {entity.getJobName(), entity.getJobStatus(), entity.getJobLock(), entity.getRunFlag(),
                entity.getJobGroup(), entity.getJobClassName(), entity.getScheduleName(), null, null};

        return updateWithinTransaction(sql, args);
    }

    @Override
    public boolean updateLockStatus(final String id, final EntranceGuard lockStatus) {
        String timeField = lockStatus == EntranceGuard.RELEASE ? controlFields[7] : controlFields[8];
        final String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                tableName(), controlFields[2], timeField, controlFields[0]);

        return updateWithinTransaction(sql, new Object[]{lockStatus, new Date(), id});
    }

    @Override
    public boolean updateJobStatus(String id, ScheduleStatus scheduleStatus) {
        String timeField = scheduleStatus == ScheduleStatus.RUNNING ? controlFields[7] : controlFields[8];
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                tableName(), controlFields[1], timeField, controlFields[0]);
        return jdbcTemplate.update(sql, scheduleStatus, new Date(), id) == 1;
    }

    @Override
    public boolean pessimisticLockQuery(final String id, final OperationCallback<JobEntity, Boolean> callback) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = false;
                try {
                    JobEntity entity = query(id, true);
                    if (entity != null) {
                        result = callback.process(entity);
                    }
                } catch (Exception e) {
                    status.setRollbackOnly();
                }
                return result;
            }
        });
    }

    @Override
    public void log(JobEntity entity) {

    }
}
