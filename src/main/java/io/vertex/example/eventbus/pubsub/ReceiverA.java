package io.vertex.example.eventbus.pubsub;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class ReceiverA extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runClusteredExample(ReceiverA.class);
	}
	
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("news-feed", message -> {
			System.out.println("received A: " + message.body());
		});
	}
}
