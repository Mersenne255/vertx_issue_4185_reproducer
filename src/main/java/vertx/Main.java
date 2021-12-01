package vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.micrometer.VertxPrometheusOptions;

public class Main {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
                new MicrometerMetricsOptions()
                        .setPrometheusOptions(
                                new VertxPrometheusOptions().setEnabled(true)).setEnabled(true)));
        Router router = Router.router(vertx);
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
        router.route("/test").handler(request -> {
            HttpServerResponse response = request.response();
            response.putHeader("content-type", "text/plain");
            response.end("Hello World!");
        });
        // Create server so we can check the metrics
        HttpServer server = vertx.createHttpServer().requestHandler(router);
        server.listen(8080);
        System.out.println("metrics: http://localhost:8080/metrics");

        // Create client that sends single request to google and then closes connection
        HttpClient httpClient = vertx.createHttpClient();
        httpClient.request(HttpMethod.GET, 80, "google.com", "")
                .flatMap(request -> request.send().flatMap(HttpClientResponse::body).onSuccess(event -> {
                    System.out.println(event);
                    request.connection().close();
                }))
        ;
    }

}
