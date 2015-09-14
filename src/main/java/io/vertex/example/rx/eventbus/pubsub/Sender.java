package io.vertex.example.rx.eventbus.pubsub;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;

public class Sender extends AbstractVerticle{
	
	@Override
	public void start() throws Exception {
		EventBus eb = vertx.eventBus();
		
		vertx.setPeriodic(1500, v->{
			eb.sendObservable("news-feed", "some news").subscribe(
					res->System.out.println("Sent: " + res.body()),
					err->System.out.println("Failed to send: " + err.getMessage()));
		});
	}
}
