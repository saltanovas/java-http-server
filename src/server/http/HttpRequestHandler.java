package server.http;

import server.RequestHandler;
import server.http.io.HttpRequestReader;
import server.http.request.HttpRequest;
import server.http.routing.HttpRouter;
import server.http.io.HttpResponseWriter;
import server.http.response.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class HttpRequestHandler implements RequestHandler {
    private final HttpRouter router;

    public HttpRequestHandler(HttpRouter router) {
        this.router = router;
    }

    @Override
    public void handle(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        HttpResponse response = HttpRequestReader.read(reader)
                .map(this::handleRequest)
                .orElseGet(this::handleInvalidRequest);

        HttpResponseWriter.writeResponse(response, writer);
        writer.flush();

        writer.close();
        reader.close();
    }

    private HttpResponse handleRequest(HttpRequest request) {
        return router.get(request)
                .map(route -> route.handle(request))
                .orElse(new HttpResponse.Builder()
                        .setStatusCode(HttpStatus.NOT_FOUND)
                        .addHeader("Content-Type", "text/plain")
                        .setBody("The " + request.getHttpMethod().getName() + " route for " + request.getUri() + " path is not found")
                        .build()
                );
    }

    private HttpResponse handleInvalidRequest() {
        return new HttpResponse.Builder()
                .setStatusCode(HttpStatus.BAD_REQUEST)
                .addHeader("Content-Type", "text/plain")
                .setBody("Failed to serve a request")
                .build();
    }
}
