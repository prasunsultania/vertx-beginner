package io.vertex.example.http.simple;

import io.vertex.example.util.GetExecutionContext;
import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	@Override
	public void start(){
		//Print the context
		GetExecutionContext.printContext();
		
		vertx.createHttpServer().requestHandler(request -> {
			request.response().
			putHeader("content-type", "text/html").
			end("<html><body><h1>Hello from vert.x!</h1></body></html>");
		}).listen(8080);
	}
}
