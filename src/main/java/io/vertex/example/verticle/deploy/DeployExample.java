package io.vertex.example.verticle.deploy;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

public class DeployExample extends AbstractVerticle{
	
	
	public static void main(String[] args) {
		Runner.runExample(DeployExample.class);
	}
	
	@Override
	public void start(){
		System.out.println("Main verticle has started, let's deploy some others...");
		
		// Deploy a verticle and don't wait for it to start
	    vertx.deployVerticle("io.vertex.example.verticle.deploy.OtherVerticle");
	    
	    System.out.println("a verticle will be started soon.");
	    
	    // Deploy another instance and  wait for it to start
	    vertx.deployVerticle("io.vertex.example.verticle.deploy.OtherVerticle", response-> {
	    	if(response.succeeded()){
	    		
	    		//result is a deploymentId
	    		String deploymentId = response.result();
	    		System.out.println("Other verticle is deployed as: " + deploymentId);
	    		
	    		//after it has started and past 2 second, lets undeploy it
	    		vertx.setTimer(10000, id -> {
	    			vertx.undeploy(deploymentId, undeployResult ->{
	    				if(undeployResult.succeeded()){
	    					System.out.println("Undeployed Successfully !");
	    				} else {
	    					System.out.println("Undeployed went bad !");
	    					undeployResult.cause().printStackTrace();
	    				}
	    			});
	    		});
	    		
	    		
	    		
	    	} else {
	    		System.out.println("f**ked up! while trying to start up");
	    		response.cause().printStackTrace();
	    	}
	    });
	    
	    System.out.println("a verticle will be started soon with handler.");
	    
	    // Deploy specifying some config
	    JsonObject config = new JsonObject().put("foo", "bar");
	    vertx.deployVerticle("io.vertex.example.verticle.deploy.OtherVerticle", new DeploymentOptions().setConfig(config));
	    
	    System.out.println("a verticle will be started soon with config.");

	    // Deploy 10 instances
	    vertx.deployVerticle("io.vertex.example.verticle.deploy.OtherVerticle", new DeploymentOptions().setInstances(10));
	    
	    System.out.println("a verticle will be started soon with 10 instances.");
	    
	    //Deploy as worker
	    vertx.deployVerticle("io.vertex.example.verticle.deploy.OtherVerticle", new DeploymentOptions().setWorker(true).setConfig(config));
	    
	    System.out.println("a verticle will be started soon as worker.");

	}
}
