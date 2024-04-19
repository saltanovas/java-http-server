import application.fibonacci.FibonacciCalculatorController;
import domain.fibonacci.FibonacciCalculator;
import server.http.HttpMethod;
import server.http.HttpServer;
import server.http.routing.Route;

import java.io.IOException;

// Add a shutdown hook to close the server when the JVM shuts down
//    HttpServer finalServer = server;
//    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//        try {
//            if (finalServer != null) finalServer.close();
//        } catch (IOException e) {
//            System.out.println("Failed to close the server: " + e.getMessage());
//        }
//    }));

void main() throws IOException {
    FibonacciCalculatorController fibonacciCalculatorController = new FibonacciCalculatorController(new FibonacciCalculator());
    Route route = new Route(HttpMethod.GET, "/fib", fibonacciCalculatorController::calculateFibonacci);

    HttpServer server = new HttpServer(8080);
    server.router.add(route);
    server.start();
}

