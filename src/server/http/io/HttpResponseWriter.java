package server.http.io;

import server.http.response.HttpResponse;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public final class HttpResponseWriter {
    private static final String HTTP_VERSION = "HTTP/1.1";

    private HttpResponseWriter() {
    }

    public static void writeResponse(HttpResponse response, Writer writer) throws IOException {
        String responseLine = buildResponseLine(response);
        String body = buildResponseBody(response);
        String responseHeaders = buildResponseHeaders(response);

        writer.write(responseLine + "\r\n" + responseHeaders + "\r\n" + body);
    }

    private static String buildResponseLine(HttpResponse response) {
        return HTTP_VERSION + " " + response.getStatusCode().value() + " " + response.getStatusCode().getReasonPhrase();
    }

    private static String buildResponseHeaders(HttpResponse response) {
        StringBuilder headers = new StringBuilder();

        response.getResponseHeaders()
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + String.join(", ", entry.getValue()))
                .forEach(header -> headers.append(header).append("\r\n"));

        if (response.getBody() != null && !response.getBody().isEmpty()) {
            headers.append("Content-Length: ")
                    .append(response.getBody().getBytes().length)
                    .append("\r\n");
        }

        return headers.toString();
    }

    private static String buildResponseBody(HttpResponse response) {
        return response.getBody() == null ? "" : new String(response.getBody().getBytes(), StandardCharsets.UTF_8);
    }
}
