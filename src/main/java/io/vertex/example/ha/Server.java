package io.vertex.example.ha;

import java.lang.management.ManagementFactory;

import io.vertex.example.util.ExampleRunner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
		ExampleRunner.runJavaExample( System.getProperty("user.dir") + "/src/main/java/", 
				Server.class, 
				new VertxOptions().setHAEnabled(true));
	}
	
	@Override
	public void start() throws Exception {
		vertx.createHttpServer().requestHandler(request -> {
			final String name = ManagementFactory.getRuntimeMXBean().getName();
			request.response().end("Happily served by " + name);
		}).listen(8080);
	}
}
