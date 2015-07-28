package io.vertex.example.eventbus.pubsub;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

public class ReceiverB extends AbstractVerticle {
	public static void main(String[] args) {
		Runner.runClusteredExample(ReceiverB.class);
	}

	@Override
	public void start() throws Exception {
		MessageConsumer<String> consumer = vertx.eventBus().consumer(
				"news-feed", message -> {
					System.out.println("received B: " + message.body());

					// for pub sub this is null, you cant reply back
				System.out.println(message.replyAddress());
			});

		consumer.completionHandler(res -> {
			if (res.succeeded()) {
				System.out.println("Reached to to all nodes!");
			} else {
				System.out.println("Failed registering handler");
			}
		});
		
		vertx.setTimer(5000,  t -> {
			
			//get value from a distributed map
			SharedData sd = vertx.sharedData();
			sd.<String, String>getClusterWideMap("mymap", res -> {
			  if (res.succeeded()) {
			    AsyncMap<String, String> map = res.result();
			    map.get("foo", resPut -> {
			    	System.out.println("Async Map");
			    	if(resPut.succeeded())
			    		System.out.println(resPut.result());
			    	else {
			    		System.out.println(resPut.cause().getMessage());
			    	}
			    });
			  } else {
			    // Something went wrong!
			  }
			});
		});

		vertx.setTimer(
				30000,
				t -> {
					//no more listening
					consumer.unregister(res -> {
						if (res.succeeded()) {
							System.out
									.println("The handler un-registration has reached all nodes");
							vertx.close(closeRes -> {
								System.out.println("closed: " + closeRes.succeeded());
							});
						} else {
							System.out.println("Un-registration failed!");
						}
					});
				});
	}
}
