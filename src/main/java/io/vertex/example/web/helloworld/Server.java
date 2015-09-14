package io.vertex.example.web.helloworld;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	@Override
	public void start() throws Exception {
		
		/**
		 * A Router is one of the core concepts of Vert.x-Web. It’s an object which maintains zero or more Routes.
		 */
		Router router = Router.router(vertx);
		
		router.route().handler(routingContext -> {
			routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
		});
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
}
