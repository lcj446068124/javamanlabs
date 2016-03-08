package sun.spring.scheduler.batch.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;

/**
 * Created by root on 2016/3/8.
 */
public class ItemJsonConvert implements ItemConvert<String, Object> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private SerializationConfig serializationConfig;

    public void setSerializationConfig(SerializationConfig serializationConfig) {
        this.serializationConfig = serializationConfig;
    }

    public ItemJsonConvert(){
        if(serializationConfig!=null){
            objectMapper.setConfig(serializationConfig);
        }
    }

    @Override
    public String convert(Object item) {
        try {
            return objectMapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
