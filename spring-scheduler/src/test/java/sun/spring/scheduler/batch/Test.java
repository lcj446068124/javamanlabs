package sun.spring.scheduler.batch;

import java.util.Arrays;

/**
 * Created by yamorn on 2016/5/12.
 */
public class Test {
    @org.junit.Test
    public void testJoin(){

        String[] jobControlFields = new String[]{
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
        String str = Arrays.toString(jobControlFields);
        System.out.println(str.replaceAll("(^\\[|\\]$)",""));

    }
}
