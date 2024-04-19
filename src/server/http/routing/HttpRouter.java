package server.http.routing;

import server.http.HttpMethod;
import server.http.request.HttpRequest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRouter {
    private final Map<String, Route> routes;

    public HttpRouter() {
        this.routes = new HashMap<>();
    }

    public HttpRouter add(Route route) {
        String routeKey = buildRouteKey(route.getMethod(), route.getPath());
        this.routes.put(routeKey, route);
        return this;
    }

    public Optional<Route> get(HttpRequest request) {
        String routeKey = buildRouteKey(request.getHttpMethod(), request.getUri().getRawPath());
        return Optional.ofNullable(routes.get(routeKey));
    }

    private String buildRouteKey(HttpMethod httpMethod, String path) {
        String normalizedPath = URI.create("/" +
                        path.replaceAll("^/+", "") +
                        (path.isEmpty() || path.matches("^/*$") ? "" : "/")
                )
                .resolve("./")
                .getPath();

        return httpMethod.getName().toUpperCase().concat(normalizedPath);
    }
}
