package io.vertex.example.metrics.http;

import io.vertex.example.util.GetExecutionContext;
import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	@Override
	public void start(){
		Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
			    new DropwizardMetricsOptions().setEnabled(true)
			));
		//Print the context
		GetExecutionContext.printContext();
		
		HttpServer server = vertx.createHttpServer().requestHandler(request -> {
			request.response().
			putHeader("content-type", "text/html").
			end("<html><body><h1>Hello from vert.x!</h1></body></html>");
		}).listen(8080);
		
		MetricsService metricsService = MetricsService.create(vertx);
		vertx.setPeriodic(5000, t -> {
		      JsonObject metrics = metricsService.getMetricsSnapshot(server);
		      System.out.println(metrics);
		    });
	}
}
