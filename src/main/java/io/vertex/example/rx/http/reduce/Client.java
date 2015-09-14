package io.vertex.example.rx.http.reduce;

import io.vertex.example.util.Runner;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
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
		
		request.toObservable()
		.flatMap(response -> {
			if(response.statusCode() != 200){
				throw new RuntimeException("UnExpected response code - " + response.statusCode());
			}
			return response.toObservable();
		})
		.doOnNext(data -> System.out.println("Chunk received"))
		//reducer adds all the Buffer into one serially
		//If there is only one chunk reduce wont be called at all
		.reduce((previousBuffer, currentBuffer)->{
			System.out.println("Previous chunk: " + previousBuffer.toString("UTF-8"));
			Buffer buf = Buffer.buffer(previousBuffer.length() + currentBuffer.length());
			return buf.appendBuffer(previousBuffer).appendBuffer(currentBuffer);
		})
		//transform buffer into string
		.map(data->data.toString("UTF-8"))
		.subscribe(
				data -> System.out.println("Received: " + data),
				error -> System.out.println("Exception: " + error.getMessage()));
		
		request.end();
	}
}
