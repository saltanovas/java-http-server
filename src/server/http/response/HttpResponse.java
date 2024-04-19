package server.http.response;

import server.http.HttpStatus;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private final Map<String, List<String>> responseHeaders;
    private final HttpStatus statusCode;
    private final String body;

    private HttpResponse(Builder builder) {
        this.responseHeaders = builder.responseHeaders;
        this.statusCode = builder.statusCode;
        this.body = builder.body;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private final Map<String, List<String>> responseHeaders = new HashMap<>();
        private HttpStatus statusCode;
        private String body;

        public Builder() {
        }

        public Builder setStatusCode(HttpStatus statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder addHeader(String name, String value) {
            responseHeaders.put(name.toLowerCase(), List.of(value));
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            addHeader("Date", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC)));
            addHeader("Server","Custom HTTP Server");
            addHeader("Content-Type", "text/plain");
            return new HttpResponse(this);
        }
    }
}
