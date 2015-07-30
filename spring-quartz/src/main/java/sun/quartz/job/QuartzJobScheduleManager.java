package sun.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import sun.quartz.utils.ApplicationContextUtils;

import java.lang.reflect.Method;

/**
 * Created by 264929 on 2015/7/30.
 */
public class QuartzJobScheduleManager extends QuartzJobBean {
    private String targetObject;
    private String targetMethod;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            Object target = ApplicationContextUtils.getBean(targetObject);

            Method m = null;
            try {
                m = target.getClass().getMethod(targetMethod);

                m.invoke(target);
            } catch (SecurityException e) {
                e.printStackTrace();
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }

    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

}
