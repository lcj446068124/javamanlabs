package sun.spring.scheduler.dao;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

/**
 * Created by yamorn on 2016/5/12.
 */
public abstract class AbstractDataAccessOperation<T,V> implements DataAccessOperation<T,V>{

    protected String tablePrefix = "ST";

    protected JdbcTemplate jdbcTemplate;

    protected PlatformTransactionManager transactionManager;

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    protected String arrayJoin(String[] array) {
        if(array == null || array.length == 0){
            return "";
        }
        return Arrays.toString(array).replaceAll("(^\\[|\\]$)", "");
    }


    protected boolean updateWithinTransaction(final String sql, final Object[] args) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                boolean result = false;
                try {
                    result = jdbcTemplate.update(sql, args) == 1;
                } catch (Exception e) {
                    status.setRollbackOnly();
                }
                return result;
            }
        });
    }

}
