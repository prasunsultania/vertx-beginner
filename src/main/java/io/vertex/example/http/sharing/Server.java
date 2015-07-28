package io.vertex.example.http.sharing;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
	    Runner.runExample(Server.class);
	}
	
	@Override
	public void start(){
		
		//deploy the server with 2 instances
		//goal is to demonstrate load sharing amongst verticles
		//mostly would be on a Round robin basis
		vertx.deployVerticle("io.vertex.example.http.sharing.HttpServerVerticle", 
				new DeploymentOptions().setInstances(2));
	}
}
