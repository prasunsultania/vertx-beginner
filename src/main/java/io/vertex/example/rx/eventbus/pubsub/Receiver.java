package io.vertex.example.rx.eventbus.pubsub;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;

public class Receiver extends AbstractVerticle{
	
	@Override
	public void start() throws Exception {
		EventBus eb = vertx.eventBus();
		
		eb.consumer("news-feed").toObservable().subscribe(
				message -> {message.reply("Ack"); System.out.println("Received: " + message.body());},
				err-> System.out.println("An exception was encountered on event bus: " + err.getMessage()));
	}
}
