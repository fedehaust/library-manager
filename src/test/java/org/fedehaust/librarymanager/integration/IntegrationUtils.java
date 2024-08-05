package org.fedehaust.librarymanager.integration;

import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class IntegrationUtils {

    public static String createRequestUrl(int port, String uri) {
        return "http://localhost:%d%s".formatted(port, uri);
    }

    public static List getBodyList(ResponseEntity<List> response) {
        List responseBody = response.getBody();

        assert responseBody != null;

        return responseBody;
    }

    public static <T> T getBody(ResponseEntity<T> response) {
        var responseBody = response.getBody();

        assert responseBody != null;
        return responseBody;
    }

    public final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
}
