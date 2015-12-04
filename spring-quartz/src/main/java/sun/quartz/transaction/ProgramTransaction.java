package sun.quartz.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by root on 2015/11/6.
 */
@Component
public class ProgramTransaction {
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final String lockSql = "select * from customer where name='Tom'";

    final String sql = "update customer set age = 23 where name='Tom'";

    public int updateCustomer() {

        return transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                jdbcTemplate.execute(lockSql);

                int result = jdbcTemplate.update(sql);
                System.out.println("===" + result);
                if(result == 1)
                    throw new RuntimeException("=======Exception");

                return result;
            }
        });
    }

    public int doTransactionInThreadPool() {
        for (int i = 0; i < 3; i++) {
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    int result = transactionTemplate.execute(new TransactionCallback<Integer>() {
                        @Override
                        public Integer doInTransaction(TransactionStatus status) {
                            jdbcTemplate.execute(lockSql);
                            int result = jdbcTemplate.update(sql);
                            System.out.println("===" + result);
                            System.out.println(status.isNewTransaction() + "===");
                            return result;
                        }
                    });
                    System.out.println("result = " + result);
                }
            });
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;

    }
}
