package server.http.routing;

import server.http.HttpMethod;
import server.http.request.HttpRequest;
import server.http.response.HttpResponse;

public class Route {
    private final HttpMethod method;
    private final String path;
    private final HttpRequestHandler handler;

    public Route(HttpMethod method, String path, HttpRequestHandler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    public HttpResponse handle(HttpRequest request) {
        return handler.handle(request);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
