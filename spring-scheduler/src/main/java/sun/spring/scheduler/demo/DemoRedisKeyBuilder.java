package sun.spring.scheduler.demo;

import sun.spring.scheduler.batch.processor.RedisKeyBuilder;

/**
 * Created by root on 2016/3/10.
 */
public class DemoRedisKeyBuilder implements RedisKeyBuilder {
    @Override
    public String build(Object item) {
        String key;
        if (item instanceof Person) {
            Person person = (Person) item;
            key = String.format("Person_%d_%s_%d", person.getId(), person.getName(), person.getAge());
            System.out.println(key);
            return key;
        } else if (item instanceof Company) {
            Company company = (Company) item;
            key = String.format("Company_%d_%s_%s", company.getId(), company.getName(), company.getAddress());
            System.out.println(key);
            return key;
        } else {
            throw new RuntimeException("No match found.");
        }
    }
}
