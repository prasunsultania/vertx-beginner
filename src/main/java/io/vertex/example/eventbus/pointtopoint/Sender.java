package io.vertex.example.eventbus.pointtopoint;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runClusteredExample(Sender.class);
	}
	
	@Override
	public void start() throws Exception {
		EventBus eb = vertx.eventBus();
		
		//every 5 second
		vertx.setPeriodic(5000, v -> {
			
			eb.send("ping-address", "ping!", reply -> {
				if(reply.succeeded()){
					System.out.println("Reply received: " + reply.result().body());
					reply.result().reply("Reply to a reply");
				} else {
					System.out.println("No reply");
				}
			});
		});
	}
}
