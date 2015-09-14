package io.vertex.example.rx.http.zip;

import rx.Observable;
import io.vertex.example.util.Runner;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;

public class Client extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Client.class);
	}
	
	@Override
	public void start() throws Exception {
		HttpClient client = vertx.createHttpClient();
		
		HttpClientRequest request1 = client.request(HttpMethod.GET, 8080, "localhost", "/");
		HttpClientRequest request2 = client.request(HttpMethod.GET, 8080, "localhost", "/");
		
		Observable<JsonObject>responseObs1 = request1.toObservable().flatMap(HttpClientResponse::toObservable).
				map(data->new JsonObject(data.toString("UTF-8")));
		Observable<JsonObject>responseObs2 = request2.toObservable().flatMap(response->response.toObservable()).
				map(data->new JsonObject(data.toString("UTF-8")));
		
		responseObs1.zipWith(responseObs2, (json1, json2) -> new JsonObject().
				put("request1", json1).
				put("request2", json2)).
				//zipwith is an another Observable
				subscribe(json -> System.out.println("Combined rESULT: " + json),
						error -> System.out.println("An exception errored: " + error.getMessage()));
		
		request1.end();
		request2.end();
	}
}
