package io.vertex.example.verticle.deploy;

import io.vertx.core.AbstractVerticle;

public class OtherVerticle extends AbstractVerticle{
	
	@Override
	public void start(){
		// The start method will be called when the verticle is deployed
		System.out.println("In OtherVerticle.start");
		System.out.println("config is: " + config());
	}
	
	@Override
	public void stop(){
		// You can optionally override the stop method too, if you have some clean-up to do
		System.out.println("In OtherVerticle.stop");
	}
}
