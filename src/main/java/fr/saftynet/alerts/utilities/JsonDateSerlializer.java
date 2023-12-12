package fr.saftynet.alerts.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonDateSerlializer {

    private static ObjectMapper mapper;

    private static Logger logger = LogManager.getLogger();

    public static ObjectMapper getInstance(){
        if (mapper == null){
            logger.debug("Instantiating ObjectMapper with date serialization");
            mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        }
        logger.debug("Getting ObjectMapper with date serialization");
        return mapper;
    }

}
