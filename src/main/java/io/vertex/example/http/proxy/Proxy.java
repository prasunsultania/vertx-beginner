package io.vertex.example.http.proxy;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;

public class Proxy extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Proxy.class);
	}
	
	/**
	 * Proxy client request to a Proxy Server
	 */
	@Override
	public void start(){
		
		//Create a client for sending HTTP
		HttpClient client = vertx.createHttpClient(new HttpClientOptions());
		
		vertx.createHttpServer().requestHandler(request -> {
			System.out.println("Proxying request: " + request.uri());
			
			//Create a client request
			HttpClientRequest c_req = client.request(request.method(), 
					8282, 
					"localhost", 
					request.uri(), c_res -> {
						//Prepare response
						System.out.println("Proxying response: " + c_res.statusCode());
						request.response().setChunked(true);
						request.response().setStatusCode(c_res.statusCode());
						request.response().headers().setAll(c_res.headers());
						
						c_res.handler(data -> {
							System.out.println("Proxying response body: " + data.toString("ISO-8859-1").length());
							request.response().write(data);
						});
						
						c_res.endHandler(v -> {
							request.response().end();
						});
			});
			
			//allow piped request
			c_req.setChunked(true);
			
			//set all headers as received
			c_req.headers().setAll(request.headers());
			
			//pass the request from client to proxy server
			request.handler(data -> {
				//System.out.println("Proxying request body " + data.toString("ISO-8859-1"));
				c_req.write(data);
			});
			
			//request is ended
			request.endHandler(v->c_req.end());
			
		}).listen(8080);
	}
}
