package io.vertex.example.eventbus.pubsub;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

public class Sender extends AbstractVerticle{
	public static void main(String[] args) {
		Runner.runClusteredExample(Sender.class);
	}
	
	@Override
	public void start() throws Exception {
		
		EventBus eb = vertx.eventBus();
		
		vertx.setPeriodic(5000, v -> {
			// there is no handler for publish unlike point to point send
			eb.publish("news-feed", "some news");
		});
		
		//set a value on distributed map
		SharedData sd = vertx.sharedData();
		sd.<String, String>getClusterWideMap("mymap", res -> {
		  if (res.succeeded()) {
		    AsyncMap<String, String> map = res.result();
		    map.put("foo", "bar", resPut -> {
		    	  if (resPut.succeeded()) {
		    	    // Successfully put the value
		    		  System.out.println("well bar was put into key foo");
		    	  } else {
		    	    // Something went wrong!
		    	  }
		    	});
		  } else {
		    // Something went wrong!
		  }
		});
	}
}
