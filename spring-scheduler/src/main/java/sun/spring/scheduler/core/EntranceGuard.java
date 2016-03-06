package sun.spring.scheduler.core;

/**
 * Created by sunyamorn on 3/6/16.
 */
public enum EntranceGuard {
    RELEASE(1),
    LOCK(2);

    private int status;
    EntranceGuard(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}
