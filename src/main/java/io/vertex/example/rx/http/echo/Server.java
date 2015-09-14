package io.vertex.example.rx.http.echo;

import io.vertex.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.HttpServerResponse;


public class Server extends AbstractVerticle {

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}

	@Override
	public void start() throws Exception {

		HttpServer server = vertx.createHttpServer();

		server.requestStream().toObservable().subscribe(req -> {
			HttpServerResponse response = req.response();
			String contentType = req.getHeader("Content-Type");
			
			if(contentType != null){
				response.putHeader("Content-Type", contentType);
			}
			response.setChunked(true);
			
			//handling data event and end on request
			req.toObservable().subscribe(data -> response.write(data), 
					error -> response.setStatusCode(500).end(),
					()-> response.end());
		});
		
		server.listenObservable(8080).subscribe(res->System.out.println("Listening on port 8080"), 
				error->System.out.println(error.getMessage()),
				()->System.out.println("listening start completed"));
	}
}
