package application.fibonacci;

import domain.fibonacci.FibonacciCalculator;
import server.http.HttpStatus;
import server.http.request.HttpRequest;
import server.http.response.HttpResponse;

public class FibonacciCalculatorController {
    final FibonacciCalculator fibonacciCalculator;

    public FibonacciCalculatorController(FibonacciCalculator fibonacciCalculator) {
        this.fibonacciCalculator = fibonacciCalculator;
    }

    public HttpResponse calculateFibonacci(HttpRequest request) {
        var n = request.getQueryParams().get("n");
        if (n == null) return buildBadRequestResponse("n query parameter is required");
        if (n.isEmpty()) return buildBadRequestResponse("n query parameter must have a value");
        if (n.size() != 1) return buildBadRequestResponse("n query parameter must have a single value");

        String nValue = n.getFirst();

        try {
            int nInt = Integer.parseInt(nValue);
            if (nInt < 0) return buildBadRequestResponse("n query parameter must be a positive integer");

            var result = fibonacciCalculator.calculate(nInt);

            return new HttpResponse.Builder()
                    .setStatusCode(HttpStatus.OK)
                    .addHeader("Content-Type", "text/plain")
                    .setBody(String.valueOf(result))
                    .build();
        } catch (NumberFormatException e) {
            return buildBadRequestResponse("n query parameter must be an integer");
        }
    }

    private HttpResponse buildBadRequestResponse(String message) {
        return new HttpResponse.Builder()
                .setStatusCode(HttpStatus.BAD_REQUEST)
                .addHeader("Content-Type", "text/plain")
                .setBody(message)
                .build();
    }
}
