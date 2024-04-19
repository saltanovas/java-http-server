package server;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Server implements Closeable {
    enum Status {READY, ACTIVE, STOPPING}

    private Status status;

    protected final RequestListener requestListener;
    private final ExecutorService requestListenerExecutorService;

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(
            final RequestListener requestListener,
            final ExecutorService requestListenerExecutorService
    ) {
        this.requestListener = requestListener;
        this.requestListenerExecutorService = requestListenerExecutorService;
        this.status = Status.READY;
    }

    public void start() {
        if (this.status != Status.READY) return;
        this.status = Status.ACTIVE;

        requestListenerExecutorService.execute(requestListener);
        logger.log(Level.INFO, "Listening for incoming connections on port " + requestListener.getPort());
    }

    @Override
    public void close() throws IOException {
        logger.log(Level.INFO, "Stopping all incoming connections on port " + requestListener.getPort());

        if (status != Status.ACTIVE) return;
        status = Status.STOPPING;

        requestListener.close();
        requestListenerExecutorService.shutdownNow();
    }
}
