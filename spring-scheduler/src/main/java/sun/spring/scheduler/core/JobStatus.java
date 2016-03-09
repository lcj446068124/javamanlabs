package sun.spring.scheduler.core;

/**
 * Created by sunyamorn on 3/6/16.
 */
public enum JobStatus {
    WAIT("WAIT"),
    RUNNING("RUNNING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    String status;

    JobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
