package server.http;

import server.RequestListener;
import server.Server;
import server.http.routing.HttpRouter;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class HttpServer extends Server {
    public final HttpRouter router;

    public HttpServer(final int port) throws IOException {
        this(port, new HttpRouter());
    }

    private HttpServer(final int port, HttpRouter router) throws IOException {
        super(
                new RequestListener(new ServerSocket(port), new HttpRequestHandler(router), Executors.newCachedThreadPool()),
                Executors.newSingleThreadExecutor()
        );
        this.router = router;
    }
}
