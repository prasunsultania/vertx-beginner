package io.vertex.example.embed;

import io.vertex.example.util.GetExecutionContext;
import io.vertx.core.Vertx;

/**
 * Run code without starting a verticle
 * @author prasunsultania
 *
 */
public class EmbeddedServer {
	public static void main(String[] args) {
		//server will run in a single thread event loop
		GetExecutionContext.printContext();
		
		//it gives a warning as well
		//Thread Thread[vert.x-eventloop-thread-1,5,main] has been blocked for 2865 ms, time limit is 2000
		Vertx.vertx().createHttpServer().requestHandler(request -> {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.response().end("Hello World!");
		}).listen(8080);
	}
}
