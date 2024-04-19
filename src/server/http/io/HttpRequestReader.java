package server.http.io;

import server.http.HttpMethod;
import server.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public final class HttpRequestReader {
    private HttpRequestReader() {
    }

    public static Optional<HttpRequest> read(BufferedReader reader) {
        return readRequestLine(reader).flatMap(HttpRequestReader::buildRequest);
    }

    private static Optional<HttpRequest> buildRequest(String requestLine) {
        String[] httpInfo = requestLine.split(" ");

        if (httpInfo.length != 3) {
            return Optional.empty();
        }

        String protocolVersion = httpInfo[2];
        if (!protocolVersion.equals("HTTP/1.1")) {
            return Optional.empty();
        }

        try {
            HttpMethod httpMethod = HttpMethod.valueOf(httpInfo[0]);
            String uri = httpInfo[1];

            HttpRequest httpRequest = new HttpRequest.Builder()
                    .setHttpMethod(httpMethod)
                    .setUri(uri)
                    .build();

            return Optional.of(httpRequest);
        } catch (IllegalArgumentException _) {
            return Optional.empty();
        }
    }

    private static Optional<String> readRequestLine(BufferedReader reader) {
        try {
            return Optional.ofNullable(reader.readLine());
        } catch (IOException _) {
            return Optional.empty();
        }
    }
}
