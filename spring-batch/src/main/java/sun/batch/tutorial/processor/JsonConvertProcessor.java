package sun.batch.tutorial.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sun.batch.tutorial.entity.Person;

/**
 * Created by root on 2016/3/3.
 */
@Component
public class JsonConvertProcessor<T> implements ItemProcessor<T, String> {
    private static final Logger logger = LoggerFactory.getLogger(JsonConvertProcessor.class);

    @Override
    public String process(T item) throws Exception {
        if (item instanceof Person) {
            Person person = (Person) item;
            logger.info("id={},name={},age={}", person.getId(), person.getName(), person.getAge());
        }

        return "hello world";
    }
}
