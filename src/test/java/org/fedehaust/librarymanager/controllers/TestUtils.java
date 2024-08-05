package org.fedehaust.librarymanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

public class TestUtils {

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    private static final ObjectMapper mapper = createObjectMapper();

    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    public final static DateFormat DATE_TIME_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00");
}
