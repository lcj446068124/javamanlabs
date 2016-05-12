package sun.spring.scheduler.core;

/**
 * Created by yamorn on 2016/5/12.
 */
public enum JobRunFlag {
    RUNNING(0),
    HANGUP(1);

    private int runFlag;

    JobRunFlag(int runFlag) {
        this.runFlag = runFlag;
    }

    public int getValue() {
        return runFlag;
    }
}
