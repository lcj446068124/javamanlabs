package sun.spring.scheduler.batch;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import sun.spring.scheduler.core.DBType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by root on 2016/3/8.
 */
public class JobCleaner {

    private static final String FIND_JOB_INSTANCE_ID_SQL = "select job_instance_id from batch_job_instance where job_name=?";

    private static final String FIND_JOB_EXECUTION_ID_SQL = "select job_execution_id from batch_job_execution where job_instance_id=?";

    private static final String FIND_STEP_EXECUTION_ID_SQL = "select step_execution_id from batch_step_execution where job_execution_id=?";

    private static final String DEL_STEP_EXECUTION_SEQ_SQL_MYSQL = "delete from batch_step_execution_seq where id=?";

    private static final String DEL_STEP_EXECUTION_CONTEXT_SQL = "delete from batch_step_execution_context where step_execution_id=?";

    private static final String DEL_STEP_EXECUTION_SQL = "delete from batch_step_execution where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_SEQ_SQL_MYSQL = "delete from batch_job_execution_seq where id=?";

    private static final String DEL_JOB_EXECUTION_PARAMS_SQL = "delete from batch_job_execution_params where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_CONTEXT_SQL = "delete from batch_job_execution_context where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_SQL = "delete from batch_job_execution where job_instance_id=?";

    private static final String DEL_JOB_INSTANCE_SQL = "delete from batch_job_instance where job_instance_id=?";

    private static final String DEL_JOB_SEQ_SQL_MYSQL = "delete from batch_job_seq where id=?";

    // fix sql

    private static final String INIT_STEP_EXECUTION_SEQ_SQL_MYSQL = "INSERT INTO BATCH_STEP_EXECUTION_SEQ values(?,0)";

    private static final String INIT_JOB_EXECUTION_SEQ_SQL_MYSQL = "INSERT INTO BATCH_JOB_EXECUTION_SEQ values(?,0)";

    private static final String INIT_JOB_SEQ_SQL_MYSQL = "INSERT INTO BATCH_JOB_SEQ values(?,0)";

    private static final String DEL_INIT_STEP_EXECUTION_SEQ_SQL_MYSQL = "DELETE FROM BATCH_STEP_EXECUTION_SEQ WHERE ID=0";

    private static final String DEL_INIT_JOB_EXECUTION_SEQ_SQL_MYSQL = "DELETE FROM BATCH_JOB_EXECUTION_SEQ WHERE ID=0";

    private static final String DEL_INIT_JOB_SEQ_SQL_MYSQL = "DELETE FROM BATCH_JOB_SEQ WHERE ID=0";

    private JdbcTemplate jdbcTemplate;

    private DBType dbType;    // MySQL, Oracle

    public JobCleaner(JdbcTemplate jdbcTemplate, DBType dbType) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbType = dbType;
    }

    public final void cleanBeforeJobLaunch(String jobName) {
        List<Long> jobInstances = findJobInstanceIdByName(jobName);
        if (jobInstances != null && jobInstances.size() > 0) {
            for (Long jobInstanceId : jobInstances) {
                Long jobExecutionId = findJobExecutionId(jobInstanceId);
                if (jobExecutionId != null) {
                    List<Long> stepExecutions = findStepExecutionId(jobExecutionId);
                    if (stepExecutions != null && stepExecutions.size() > 0) {
                        for (Long stepExecutionId : stepExecutions) {
                            if (dbType == DBType.MYSQL ) {
                                delete(DEL_STEP_EXECUTION_SEQ_SQL_MYSQL, stepExecutionId);
                            }
                            delete(DEL_STEP_EXECUTION_CONTEXT_SQL, stepExecutionId);
                        }
                    }
                    delete(DEL_STEP_EXECUTION_SQL, jobExecutionId);
                    if (dbType == DBType.MYSQL) {
                        delete(DEL_JOB_EXECUTION_SEQ_SQL_MYSQL, jobExecutionId);
                    }
                    delete(DEL_JOB_EXECUTION_PARAMS_SQL, jobExecutionId);
                    delete(DEL_JOB_EXECUTION_CONTEXT_SQL, jobExecutionId);
                }
                delete(DEL_JOB_EXECUTION_SQL, jobInstanceId);
                delete(DEL_JOB_INSTANCE_SQL, jobInstanceId);
                if (dbType == DBType.MYSQL) {
                    delete(DEL_JOB_SEQ_SQL_MYSQL, jobInstanceId);
                }
//                // init seq to fix  Exception: Duplicate entry '0' for key 'PRIMARY' when insert value to BATCH_JOB_INSTANCE
                if (dbType == DBType.MYSQL) {
                    jdbcTemplate.update(INIT_STEP_EXECUTION_SEQ_SQL_MYSQL, jobInstanceId);
                    jdbcTemplate.update(INIT_JOB_EXECUTION_SEQ_SQL_MYSQL, jobInstanceId);
                    jdbcTemplate.update(INIT_JOB_SEQ_SQL_MYSQL, jobInstanceId);
                }
            }
        }else{
            // init seq to fix  Exception: Duplicate entry '0' for key 'PRIMARY' when insert value to BATCH_JOB_INSTANCE
            if (dbType == DBType.MYSQL) {
                //delete
                jdbcTemplate.execute(DEL_INIT_STEP_EXECUTION_SEQ_SQL_MYSQL);
                jdbcTemplate.execute(DEL_INIT_JOB_EXECUTION_SEQ_SQL_MYSQL);
                jdbcTemplate.execute(DEL_INIT_JOB_SEQ_SQL_MYSQL);
                // insert
                jdbcTemplate.update(INIT_STEP_EXECUTION_SEQ_SQL_MYSQL, 0);
                jdbcTemplate.update(INIT_JOB_EXECUTION_SEQ_SQL_MYSQL, 0);
                jdbcTemplate.update(INIT_JOB_SEQ_SQL_MYSQL, 0);
            }
        }
    }

    private List<Long> findJobInstanceIdByName(String jobName) {
        return jdbcTemplate.queryForList(FIND_JOB_INSTANCE_ID_SQL, Long.TYPE, jobName);
    }

    private Long findJobExecutionId(long jobInstanceId) {
        return jdbcTemplate.queryForObject(FIND_JOB_EXECUTION_ID_SQL, new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(1);
            }
        }, jobInstanceId);
    }

    private List<Long> findStepExecutionId(long jobExecutionId) {
        return jdbcTemplate.queryForList(FIND_STEP_EXECUTION_ID_SQL, Long.TYPE, jobExecutionId);
    }


    private void delete(String sql, long id) {
        jdbcTemplate.update(sql, id);
    }


}
