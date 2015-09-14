package io.vertex.example.rx.http.simple;

import io.vertex.example.util.Runner;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;

public class Client extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Client.class);
	}
	
	@Override
	public void start() throws Exception {
		
		HttpClient client = vertx.createHttpClient();
		
		HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/");
		
		request.toObservable().flatMap(response -> {
			if(response.statusCode() != 200){
				throw new RuntimeException("Response code isn't 200!");
			}
			return response.toObservable();
		}).subscribe(
				data->System.out.println("Resp Data: " + data.toString("UTF-8")),
				error->System.out.println(error.getMessage()));
		
		request.end();
		
	}
}
