package top.wangudiercai.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperFactory {
    private static ObjectMapper instance;

    public static ObjectMapper getInstance() {
        if (instance == null) {
            synchronized (ObjectMapperFactory.class){
                if (instance==null){
                    instance = new ObjectMapper();
                    instance.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                    instance.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                    instance.registerModule(new JavaTimeModule());
                }
            }
        }
        return instance;
    }
}
