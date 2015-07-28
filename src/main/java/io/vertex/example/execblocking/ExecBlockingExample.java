package io.vertex.example.execblocking;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class ExecBlockingExample extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(ExecBlockingExample.class);
	}
	
	@Override
	public void start() throws Exception {
		
		vertx.createHttpServer().requestHandler(request -> {
			//here a separate thread would run, an alternate way is to use worker verticle
			vertx.<String>executeBlocking(future -> {
				try{
					//This is an imaginary blocking operation
					Thread.sleep(10000);
				} catch(Exception e){
					
				}
				String result = "cool!";
				//complete the async operation
				future.complete(result);
			}, result -> {
				if(result.succeeded()){
					request.response().putHeader("content-type", "text/plain").end(result.result());
				} else {
					result.cause().printStackTrace();
					request.response().setStatusCode(500).end();
				}
			});
		}).listen(8080);
	}
}
