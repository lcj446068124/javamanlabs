package sun.spring.scheduler.batch.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by root on 2016/3/3.
 */
public class JsonItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List<? extends T> items) throws Exception {
        for(T t : items){
            System.out.println(t);
        }
    }
}
