package sun.spring.scheduler.core;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import sun.spring.scheduler.domain.JobEntity;
import sun.spring.scheduler.domain.TaskEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * Created by sunyamorn on 3/7/16.
 */
public class ScheduleJobConfig {

    private String tablePrefix = "ST_";

    public static final String TABLE_JOB_CONTROL = "JOB_CONTROL";

    public static final String TABLE_TASK_CONTROL = "TASK_CONTROL";

    private PlatformTransactionManager transactionManager;

    private JdbcTemplate jdbcTemplate;

    private ThreadPoolTaskScheduler executorService;

    public static final String[] jobControlFields = new String[]{"JOB_NAME", "JOB_STATUS", "JOB_LOCK", "JOB_GROUP", "JOB_CLASS_NAME", "SCHEDULE_NAME", "LAST_START_TIME", "LAST_END_TIME"};

    public static final String[] taskControlFields = new String[]{"TASK_NAME", "TASK_STATUS", "TASK_LOCK", "TASK_GROUP", "TASK_CLASS_NAME", "SCHEDULE_NAME", "LAST_START_TIME", "LAST_END_TIME"};


    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ThreadPoolTaskScheduler getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ThreadPoolTaskScheduler executorService) {
        this.executorService = executorService;
    }

    /**
     *
     * Task Persistence
     */
    public final TaskEntity queryTask(String taskName) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", join(taskControlFields), tablePrefix + TABLE_TASK_CONTROL, taskControlFields[0]);
        return queryTask(sql, taskName);
    }

    public final TaskEntity lockTaskRow(String taskName) {
        String lockSql = String.format("SELECT * FROM %s WHERE %s = ? FOR UPDATE", tablePrefix + TABLE_TASK_CONTROL, taskControlFields[0]);
        return queryTask(lockSql, taskName);
    }

    public final boolean insertTask(String taskName, TaskStatus taskStatus, int lock, String taskGroup, String taskClassName, String scheduleName) {
        String insertSql = String.format("INSERT INTO %s (%s) VALUES (?,?,?,?,?,?,?,?)", tablePrefix + TABLE_TASK_CONTROL, join(taskControlFields));
        return jdbcTemplate.update(insertSql, taskName, taskStatus.getStatus(), lock, taskGroup, taskClassName, scheduleName, null, null) == 1;
    }

    public final boolean updateTaskGuardStatus(int status, String timeField, String taskName) {
        String updateSql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tablePrefix + TABLE_TASK_CONTROL, taskControlFields[2], timeField, taskControlFields[0]);
        return jdbcTemplate.update(updateSql, status, new Date(), taskName) == 1;
    }

    public final boolean validationTask() {
        String updateSql = String.format("UPDATE %s SET %s = ?", tablePrefix + TABLE_TASK_CONTROL, taskControlFields[2]);
        return jdbcTemplate.update(updateSql, EntranceGuard.RELEASE.getStatus()) == 1;
    }

    public final boolean updateTaskTimeAndStatus(String status,String timeField, String jobName){
        String updateSql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tablePrefix + TABLE_TASK_CONTROL, taskControlFields[1], timeField, taskControlFields[0]);
        return jdbcTemplate.update(updateSql, status, new Date(), jobName) == 1;
    }

    private String join(String[] array) {
        String result = "";
        for (String str : array) {
            result += str + ",";
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : result;
    }

    public final TaskEntity queryTask(String sql, String taskName) {
        try {
            return jdbcTemplate.queryForObject(
                    sql, new Object[]{taskName}, new RowMapper<TaskEntity>() {
                        @Override
                        public TaskEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                            TaskEntity taskEntity = new TaskEntity();
                            taskEntity.setTaskName(rs.getString(taskControlFields[0]));
                            taskEntity.setTaskGroup(rs.getString(taskControlFields[3]));
                            taskEntity.setStatus(rs.getString(taskControlFields[1]));
                            taskEntity.setScheduleName(rs.getString(taskControlFields[5]));
                            taskEntity.setLock(rs.getInt(taskControlFields[2]));
                            taskEntity.setTaskClassName(rs.getString(taskControlFields[4]));
                            taskEntity.setStartTime(rs.getDate(taskControlFields[6]));
                            taskEntity.setEndTime(rs.getDate(taskControlFields[7]));
                            return taskEntity;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Job Persistence
     */

    public final JobEntity queryJob(String jobName) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", join(jobControlFields), tablePrefix + TABLE_JOB_CONTROL, jobControlFields[0]);
        return queryJob(sql, jobName);
    }

    public final JobEntity lockJobRow(String jobName) {
        String lockSql = String.format("SELECT * FROM %s WHERE %s = ? FOR UPDATE", tablePrefix + TABLE_JOB_CONTROL, jobControlFields[0]);
        return queryJob(lockSql, jobName);
    }

    public final boolean insertJob(String jobName, JobStatus jobStatus, int lock, String jobGroup, String jobClassName, String scheduleName) {
        String insertSql = String.format("INSERT INTO %s (%s) VALUES (?,?,?,?,?,?,?,?)", tablePrefix + TABLE_JOB_CONTROL, join(jobControlFields));
        return jdbcTemplate.update(insertSql, jobName, jobStatus.getStatus(), lock, jobGroup, jobClassName, scheduleName, null, null) == 1;
    }

    public final boolean updateJobGuardStatus(int status, String timeField, String jobName) {
        String updateSql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tablePrefix + TABLE_JOB_CONTROL, jobControlFields[2], timeField, jobControlFields[0]);
        return jdbcTemplate.update(updateSql, status, new Date(), jobName) == 1;
    }

    public final boolean validationJob() {
        String updateSql = String.format("UPDATE %s SET %s = ?", tablePrefix + TABLE_JOB_CONTROL, jobControlFields[2]);
        return jdbcTemplate.update(updateSql, EntranceGuard.RELEASE.getStatus()) == 1;
    }

    public final boolean updateJobTimeAndStatus(String status,String timeField, String jobName){
        String updateSql = String.format("UPDATE %s SET %s = ?, %s = ? WHERE %s = ?", tablePrefix + TABLE_JOB_CONTROL, jobControlFields[1], timeField, jobControlFields[0]);
        return jdbcTemplate.update(updateSql, status, new Date(), jobName) == 1;
    }


    public final JobEntity queryJob(String sql, String jobName) {
        try {
            return jdbcTemplate.queryForObject(
                    sql, new Object[]{jobName}, new RowMapper<JobEntity>() {
                        @Override
                        public JobEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                            JobEntity jobEntity = new JobEntity();
                            jobEntity.setJobName(rs.getString(jobControlFields[0]));
                            jobEntity.setJobGroup(rs.getString(jobControlFields[3]));
                            jobEntity.setStatus(rs.getString(jobControlFields[1]));
                            jobEntity.setScheduleName(rs.getString(jobControlFields[5]));
                            jobEntity.setLock(rs.getInt(jobControlFields[2]));
                            jobEntity.setJobClassName(rs.getString(jobControlFields[4]));
                            jobEntity.setStartTime(rs.getDate(jobControlFields[6]));
                            jobEntity.setEndTime(rs.getDate(jobControlFields[7]));
                            return jobEntity;
                        }
                    }
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
