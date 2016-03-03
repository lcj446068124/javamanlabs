package sun.batch.tutorial.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sun.batch.tutorial.entity.Person;

/**
 * Created by root on 2016/3/3.
 */
@Component
public class DemoProcessor implements ItemProcessor<Person, String> {
    @Override
    public String process(Person item) throws Exception {
        System.out.println("hello");
        return item.getId() + "  ===  "+item.getName();
    }
}
