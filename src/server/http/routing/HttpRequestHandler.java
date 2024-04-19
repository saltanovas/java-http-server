package server.http.routing;

import server.http.request.HttpRequest;
import server.http.response.HttpResponse;

@FunctionalInterface
public interface HttpRequestHandler {
    HttpResponse handle(HttpRequest request);
}
