package io.vertex.example.verticle.worker;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class MainVerticle extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(MainVerticle.class);
	}
	
	@Override
	public void start(){
		System.out.println("[Main] Running in " + Thread.currentThread().getName());
		
		//deploy verticle
		vertx.deployVerticle("io.vertex.example.verticle.worker.WorkerVerticle",
				new DeploymentOptions().setWorker(true),
				result -> {
					if(result.failed()){
						System.out.println("Deployment of worker failed - " + result.cause().getMessage());
					}
				});
		
		//after every 5 second
		vertx.setPeriodic(5000, time -> {
			//send message on event bus with address sample.data
			vertx.eventBus().send("sample.data", 
					"hello vert.x",
					//After you receive a reply
					r -> {
						System.out.println("[Main] Receiving reply ' " + r.result().body().toString()
					              + "' in " + Thread.currentThread().getName());
					});
		});
		
	}
}
