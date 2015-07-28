package io.vertex.example.eventbus.pointtopoint;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Receiver extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runClusteredExample(Receiver.class);
	}
	
	@Override
	public void start() throws Exception {
		EventBus eb = vertx.eventBus();
		
		eb.consumer("ping-address", message -> {
			
			System.out.println("Received Message: " + message.body());
			
			//Reply back
			message.reply("pong!", replyBack-> {
				//if there is a further reply on reply
				if(replyBack.succeeded())
					System.out.println("Reply sucess :: " + replyBack.result().body());
			});
		});
	}
}
