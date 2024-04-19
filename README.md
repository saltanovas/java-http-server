# Java HTTP Server

This is a simple HTTP server written in Java.

## Requirements

- Java 22

## Usage

```java
HttpServer server = new HttpServer(8080);

Route route = new Route(HttpMethod.GET, "/fib", (request) -> {
    return new HttpResponse.Builder()
            .setStatusCode(HttpStatus.OK)
            .addHeader("Content-Type", "text/plain")
            .setBody("Hello, World!")
            .build();
});

server.addRoute(route);
server.start();
```

## Notes

- The server is able to shut down gracefully
- The following paths are treated as the same: `/test/path`, `test/path`, `/test/path/`, `test/path/`, `//test//path//` etc.

## TODO

- [ ] Dokerize the server
- [ ] Move Fibonacci number validation to domain
- [ ] Cover the server with tests
- [ ] Add support for URL path variables
- [ ] Add shutdown hook
- [ ] Ability to register exception handlers: route is not found, internal server error etc.
- [ ] Add linter
