package sun.batch.tutorial.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

/**
 * Created by root on 2016/3/8.
 */
public class JobValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        System.out.println("validator=" + parameters.getString("jobName"));
    }
}
