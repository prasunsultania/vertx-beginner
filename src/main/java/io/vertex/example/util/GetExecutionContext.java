package io.vertex.example.util;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class GetExecutionContext {
	public static void printContext(){
		Context context = Vertx.vertx().getOrCreateContext();
		
		if (context.isEventLoopContext() && Context.isOnVertxThread()) {
			  System.out.println("Context attached to Event Loop and Vertx Thread");
			} else if(context.isEventLoopContext()){
				System.out.println("Context attached to Event Loop without Vertx Thread");
			}
			else if (context.isWorkerContext()) {
			  System.out.println("Context attached to Worker Thread");
			} else if (context.isMultiThreadedWorkerContext()) {
			  System.out.println("Context attached to Worker Thread - multi threaded worker");
			} else if (! Context.isOnVertxThread()) {
			  System.out.println("Context not attached to a thread managed by vert.x");
			}
	}
}
