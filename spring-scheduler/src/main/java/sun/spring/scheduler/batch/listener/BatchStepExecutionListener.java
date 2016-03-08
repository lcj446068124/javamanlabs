package sun.spring.scheduler.batch.listener;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

/**
 * Created by root on 2016/3/8.
 */
public class BatchStepExecutionListener extends StepExecutionListenerSupport {

    private SqlSessionFactory sqlSessionFactory;

    private String countQueryId;

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public void setCountQueryId(String countQueryId) {
        this.countQueryId = countQueryId;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        assert sqlSessionFactory != null;

        long total = 0;
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            total = sqlSession.selectOne(countQueryId);
        } finally {
            sqlSession.close();
        }
        stepExecution.getExecutionContext().putLong("total", total);
    }

}
