package server.http;

public final class HttpMethod {
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");

    private final String name;

    private HttpMethod(String name) {
        this.name = name.toUpperCase();
    }

    public static HttpMethod valueOf(String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "HEAD" -> HEAD;
            default -> new HttpMethod(method);
        };
    }

    public String getName() {
        return name;
    }
}
