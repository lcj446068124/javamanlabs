package sun.spring.scheduler.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import sun.spring.scheduler.core.EntranceGuard;
import sun.spring.scheduler.core.JobRunFlag;
import sun.spring.scheduler.core.ScheduleStatus;
import sun.spring.scheduler.domain.JobEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<JobEntity>() {
                JobEntity entity = new JobEntity();

                @Override
                public JobEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                    entity.setJobName(rs.getString(controlFields[0]));
                    entity.setJobStatus(rs.getString(controlFields[1]));
                    entity.setJobLock(rs.getInt(controlFields[2]));
                    entity.setRunFlag(rs.getInt(controlFields[3]));
                    entity.setJobGroup(rs.getString(controlFields[4]));
                    entity.setJobClassName(rs.getString(controlFields[5]));
                    entity.setScheduleName(rs.getString(controlFields[6]));
                    entity.setLastStartTime(rs.getDate(controlFields[7]));
                    entity.setLastEndTime(rs.getDate(controlFields[8]));
                    return entity;
                }
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<JobEntity> queryAll() {
        String sql = String.format("SELECT * FROM %s", tableName());
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        List<JobEntity> list = new ArrayList<>();
        for (Map<String, Object> map : results) {
            JobEntity entity = new JobEntity();
            entity.setJobName(convert(map.get(controlFields[0]), String.class));
            entity.setJobStatus(convert(map.get(controlFields[1]), String.class));
            entity.setJobLock(convert(map.get(controlFields[2]), Integer.class));
            entity.setRunFlag(convert(map.get(controlFields[3]), Integer.class));
            entity.setJobGroup(convert(map.get(controlFields[4]), String.class));
            entity.setJobClassName(convert(map.get(controlFields[5]), String.class));
            entity.setScheduleName(convert(map.get(controlFields[6]), String.class));
            entity.setLastStartTime(convert(map.get(controlFields[7]), Date.class));
            entity.setLastEndTime(convert(map.get(controlFields[8]), Date.class));
            list.add(entity);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private <T> T convert(Object value, Class<T> type) {
        return value != null ? (T) value : null;
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

        return updateWithinTransaction(sql, new Object[]{lockStatus.getStatus(), new Date(), id});
    }

    @Override
    public boolean updateJobStatus(String id, ScheduleStatus scheduleStatus) {
        String timeField = scheduleStatus == ScheduleStatus.RUNNING ? controlFields[7] : controlFields[8];
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                tableName(), controlFields[1], timeField, controlFields[0]);
        return jdbcTemplate.update(sql, scheduleStatus.getStatus(), new Date(), id) == 1;
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
    public boolean releaseJobLock(String id) {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?",
                tableName(), controlFields[2], controlFields[1], controlFields[0]);
        return updateWithinTransaction(sql,
                new Object[]{EntranceGuard.RELEASE.getStatus(), ScheduleStatus.WAIT.getStatus(), id});
    }

    @Override
    public boolean hangup(String id, int flag) {
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?",
                tableName(), controlFields[3], controlFields[0]);
        return updateWithinTransaction(sql,
                new Object[]{(flag == 0 ? JobRunFlag.RUNNING.getValue() : JobRunFlag.HANGUP.getValue()), id});
    }

    @Override
    public void log(JobEntity entity) {

    }
}
