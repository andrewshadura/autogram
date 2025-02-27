package digital.slovensko.autogram.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class DocumentationEndpoint implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean isYaml = exchange.getRequestURI().getPath().endsWith(".yml");
        var mimeType = isYaml ? "text/yaml" : "text/html";
        var filename = isYaml ? "server.yml" : "index.html";

        exchange.getResponseHeaders().set("Content-Type", mimeType);

        // automatically closes both streams
        try (var stream = getClass().getResourceAsStream(filename);
             var responseStream = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, 0);

            requireNonNull(stream).transferTo(responseStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
