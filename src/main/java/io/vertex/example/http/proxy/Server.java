package io.vertex.example.http.proxy;

import io.vertex.example.util.InputHandler;
import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class Server extends AbstractVerticle{
	
	/**
	 * Util for running via IDE
	 * @param args
	 */
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	/**
	 * A simple server that simply counts the number of chunks received 
	 */
	@Override
	public void start(){
		vertx.createHttpServer().requestHandler(request-> {
			
			System.out.println("Absolute URI: " + request.absoluteURI());
			System.out.println("URI: " + request.uri());
			
			InputHandler counter = new InputHandler();
			request.headers().forEach(header->{
				System.out.println("Header: " + header.getKey() + ". Value: " + header.getValue());
			});
			
			//whenever a chunk of data is received
			request.handler(data -> {
				counter.increment();
				System.out.println("A data chunk is received on Server : " + data.toString("ISO-8859-1").length());
				//System.out.println("Got data " + data.toString("ISO-8859-1"));
			});
			
			//whenever request ends
			request.endHandler(v->{
				//v is null
				System.out.println(v == null);
				System.out.println("total # of chunks :" + counter.getCount());
				request.response().setChunked(true);
				
				for (int i = 0; i < counter.getCount() ; i++) {
		          request.response().write("server-data-chunk-" + i);
		        }

		        request.response().end();
			});
			
		}).listen(8282);
	}
}
