package server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestListener implements Runnable, Closeable {
    public final ServerSocket serverSocket;
    private final RequestHandler handler;
    private final ExecutorService requestHandlerExecutorService;
    private boolean isClosed;

    private static final Logger logger = Logger.getLogger(RequestListener.class.getName());

    public RequestListener(
            final ServerSocket serverSocket,
            final RequestHandler handler,
            final ExecutorService requestHandlerExecutorService
    ) {
        this.serverSocket = serverSocket;
        this.handler = handler;
        this.requestHandlerExecutorService = requestHandlerExecutorService;
        this.isClosed = false;
    }

    @Override
    public void run() {
        while (!isClosed && !serverSocket.isClosed()) {
            try {
                final Socket socket = serverSocket.accept();
                requestHandlerExecutorService.execute(() -> {
                    try (socket) {
                        handler.handle(socket);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Failed to handle a request", e);
                    }
                });
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to accept a connection", e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        isClosed = true;

        serverSocket.close();

        requestHandlerExecutorService.shutdown();
        try(serverSocket) {
            logger.log(Level.INFO, "Gracefully terminating all executing requests.");
            if (!requestHandlerExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.log(Level.WARNING, "The graceful termination of all executing requests has failed. Forcing termination.");
                requestHandlerExecutorService.shutdownNow();
                if (!requestHandlerExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.log(Level.WARNING, "The force termination of all executing requests has failed.");
                }
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "The request listener termination has failed upon await.", e);
        }
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
