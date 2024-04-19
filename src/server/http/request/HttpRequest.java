package server.http.request;

import server.http.HttpMethod;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final URI uri;

    private HttpRequest(Builder builder) {
        httpMethod = builder.httpMethod;
        uri = builder.uri;
    }

    public URI getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Map<String, List<String>> getQueryParams() {
        final String rawQuery = getUri().getRawQuery();

        if (rawQuery == null) {
            return Collections.emptyMap();
        }

        return Arrays.stream(rawQuery.split("&"))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        keyValue -> URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        keyValue -> keyValue.length == 2
                                ? Arrays.stream(keyValue[1].split(","))
                                .map(value -> URLDecoder.decode(value, StandardCharsets.UTF_8))
                                .collect(Collectors.toList())
                                : new ArrayList<>(),
                        (oldList, newList) -> {
                            oldList.addAll(newList);
                            return oldList;
                        }
                ));
    }

    public static class Builder {
        private HttpMethod httpMethod;
        private URI uri;

        public Builder() {
        }

        public Builder setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder setUri(String uri) {
            this.uri = URI.create(uri.replaceAll("^/+", "/"));
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
