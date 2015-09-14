package io.vertex.example.rx.http.zip;

import io.vertex.example.util.Runner;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

public class Server extends AbstractVerticle {
	
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	@Override
	public void start() throws Exception {
		HttpServer server = vertx.createHttpServer();
		server.requestStream().toObservable().subscribe(
				request->{
					request.response()
					.putHeader("Content-Type", "application/json")
					.end(new JsonObject().put("time", System.currentTimeMillis()).toString());
				});
		
		server.listenObservable(8080).subscribe(
				message->System.out.println("Yeah, listening now: " + message),
				error->System.out.println("Failed to start server: " + error.getMessage()));
	}
}
