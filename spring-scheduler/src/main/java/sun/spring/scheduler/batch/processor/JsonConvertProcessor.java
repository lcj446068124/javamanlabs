package sun.spring.scheduler.batch.processor;

import org.springframework.batch.item.ItemProcessor;

/**
 * Created by root on 2016/3/3.
 */
public class JsonConvertProcessor implements ItemProcessor<Object, Tuple<String, Object>> {

    private ItemConvert<String, Object> jsonConvert;

    public void setJsonConvert(ItemConvert<String, Object> jsonConvert) {
        this.jsonConvert = jsonConvert;
    }

    @Override
    public Tuple<String, Object> process(Object item) throws Exception {
        assert jsonConvert != null;

        String json = jsonConvert.convert(item);
        System.out.println("processor "+json);
        Thread.sleep(1000);
        return new Tuple<>(json, item);
    }


}
