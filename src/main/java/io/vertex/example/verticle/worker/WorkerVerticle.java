package io.vertex.example.verticle.worker;

import io.vertx.core.AbstractVerticle;

public class WorkerVerticle extends AbstractVerticle{
	
	public void start(){
		System.out.println("[Worker] Starting in " + Thread.currentThread().getName());
		
		//consume all events of sample.data
		//reply back with the same message
		vertx.eventBus().consumer("sample.data", message -> {
			System.out.println("[Worker] Consuming in " + Thread.currentThread().getName());
			
			String body = message.body().toString();
			message.reply(body.toUpperCase());
		});
	}
}
