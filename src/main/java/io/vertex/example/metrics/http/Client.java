package io.vertex.example.metrics.http;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class Client extends AbstractVerticle{
	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
	    Runner.runExample(Client.class);
	}
	
	@Override
	public void start(){
		//Poll server every 1 second
		vertx.setPeriodic(2500, l -> {
			vertx.createHttpClient().getNow(8080, 
					"localhost",
					"/",
					resp -> {
						resp.bodyHandler(body -> {
				              System.out.println(body.toString("ISO-8859-1"));
				         });
					});
		});
	}
}
