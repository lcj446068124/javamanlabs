package sun.spring.scheduler.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sun.spring.scheduler.demo.Company;
import sun.spring.scheduler.demo.Person;

/**
 * Created by root on 2016/3/3.
 */
public class JsonConvertProcessor<T> implements ItemProcessor<T, String> {
    private static final Logger logger = LoggerFactory.getLogger(JsonConvertProcessor.class);

    @Override
    public String process(T item) throws Exception {
        if (item instanceof Person) {
            Person person = (Person) item;
            logger.info("class={},id={},name={},age={}", person.getClass().getName(), person.getId(), person.getName(), person.getAge());
        } else if (item instanceof Company) {
            Company company = (Company) item;
            logger.info("class={}, id={},name={}", company.getClass().getName(), company.getId(), company.getName());

        }

        return "hello world";
    }
}
