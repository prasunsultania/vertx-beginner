package io.vertex.example.http.simple;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class Client extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Client.class);
	}
	
	@Override
	public void start(){
		vertx.createHttpClient().getNow(
				8080, "localhost", "/", 
				resp->{
					System.out.println("Got repsonse::" + resp.statusCode());
					resp.bodyHandler(body -> {
						System.out.println("Got data " + body.toString("ISO-8859-1"));
					});
				});
	}
}
