package sun.spring.scheduler.batch;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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

    private static final String DEL_STEP_EXECUTION_SEQ_SQL = "delete from batch_step_execution_seq where id=?";

    private static final String DEL_STEP_EXECUTION_CONTEXT_SQL = "delete from batch_step_execution_context where step_execution_id=?";

    private static final String DEL_STEP_EXECUTION_SQL = "delete from batch_step_execution where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_SEQ_SQL = "delete from batch_job_execution_seq where id=?";

    private static final String DEL_JOB_EXECUTION_PARAMS_SQL = "delete from batch_job_execution_params where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_CONTEXT_SQL = "delete from batch_job_execution_context where job_execution_id=?";

    private static final String DEL_JOB_EXECUTION_SQL = "delete from batch_job_execution where job_instance_id=?";

    private static final String DEL_JOB_INSTANCE_SQL = "delete from batch_job_instance where job_instance_id=?";

//    private static final String DEL_SQL_SQL = "delete from batch_job_instance where job_instance_id=?";

    private JdbcTemplate jdbcTemplate;

    public JobCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final void cleanBeforeJobLaunch(String jobName) {
        List<Long> jobInstances = findJobInstanceIdByName(jobName);
        if (jobInstances != null && jobInstances.size() > 0) {
            for (Long jobInstanceId : jobInstances) {
                Long jobExecutionId = findJobExecutionId(jobInstanceId);
                if (jobExecutionId != null) {
                    List<Long> stepExecutions = findStepExecutionId(jobExecutionId);
                    if(stepExecutions!=null && stepExecutions.size()>0){
                        for (Long stepExecutionId : stepExecutions) {
                            delete(DEL_STEP_EXECUTION_SEQ_SQL, stepExecutionId);
                            delete(DEL_STEP_EXECUTION_CONTEXT_SQL, stepExecutionId);
                        }
                    }

                    delete(DEL_STEP_EXECUTION_SQL, jobExecutionId);
                    delete(DEL_JOB_EXECUTION_SEQ_SQL, jobExecutionId);
                    delete(DEL_JOB_EXECUTION_PARAMS_SQL, jobExecutionId);
                    delete(DEL_JOB_EXECUTION_CONTEXT_SQL, jobExecutionId);
                }
                delete(DEL_JOB_EXECUTION_SQL, jobInstanceId);
                delete(DEL_JOB_INSTANCE_SQL, jobInstanceId);
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
