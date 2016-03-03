package sun.batch.tutorial.service;

import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.batch.tutorial.entity.Person;
import sun.batch.tutorial.itemwriter.JsonItemWriter;

/**
 * Created by root on 2016/3/3.
 */
@Service
public class BatchDemoServiceImpl implements BatchDemoService {
    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyBatisBatchItemWriter itemWriter;

    @Autowired
    private MyBatisPagingItemReader<Person> itemReader;

    @Autowired
    private ItemProcessor<Person, String> demoProcessor;

    @Autowired
    private ItemWriter<String> jsonItemWriter;

    @Override
    public JobExecution transfer() {
        itemReader.setQueryId("sun.batch.tutorial.entity.Person.list");

        Step step = stepBuilderFactory.get("step1").<Person, String>chunk(1000)
                .reader(itemReader)
                .processor(demoProcessor)
                .writer(jsonItemWriter)
                .faultTolerant().skipLimit(10).skip(NullPointerException.class)
                .build();
        Job job = jobBuilderFactory.get("job1").incrementer(new RunIdIncrementer()).start(step).build();
        JobParameters parameters = new JobParametersBuilder().toJobParameters();
        try {
            return  jobLauncher.run(job, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
