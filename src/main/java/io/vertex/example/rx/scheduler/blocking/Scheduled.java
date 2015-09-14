package io.vertex.example.rx.scheduler.blocking;

import rx.Observable;
import rx.Scheduler;
import rx.plugins.RxJavaSchedulersHook;
import io.vertex.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;

public class Scheduled extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Scheduled.class);
	}
	
	private String blockingLoad(String id, String eS) {
	    // Simulate a blocking action
	    try {
	      Thread.sleep(100);
	      System.out.println("In thread ("+ eS +"sleep): " + Thread.currentThread());
	      return "someData for " + id;
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	
	@Override
	public void start() throws Exception {
		System.out.println("In thread: " + Thread.currentThread());
		
		//When working with scheduler it is important to understand where exactly your tasks are running
		//and where your events are being listened
		
		//Below is syntax for registering a blocking scheduler, which will always execute in worker thread
		//Scheduler scheduler = io.vertx.rxjava.core.RxHelper.blockingScheduler(vertx);
		
		RxJavaSchedulersHook hook = RxHelper.schedulerHook(vertx);
	    rx.plugins.RxJavaPlugins.getInstance().registerSchedulersHook(hook);
		
	    /**Usinf IO Scheduler which uses Worker threads to run tasks and perform listener action**/
		// This scheduler can execute blocking actions
		//Blocking scheduler would happen in vertx worker thread pool, not in event loop
		Observable.just("someID1", "someID2", "someID3", "someID4")
	    // All operations done on the observer now can be blocking
	    .observeOn(hook.getIOScheduler())
	    // Load from a blocking api
	    .map(id -> blockingLoad(id, ""))
	    .subscribe(item -> {
	    	System.out.println("In thread (subscribe): " + Thread.currentThread());
	      System.out.println("Got item " + item);
	    }, err -> {
	      err.printStackTrace();
	    }, () -> {
	    	System.out.println("In thread (done): " + Thread.currentThread());
	      System.out.println("Done");
	    });
	    
	    /**Using Computation Scheduler, which uses the event loop to run tasks and perform listener action**/
	    Observable.just("someIDE1", "someIDE2", "someIDE3", "someIDE4")
	    .observeOn(hook.getComputationScheduler())
	    // Load from a blocking api
	    .map(id -> blockingLoad(id, "E-"))
	    .subscribe(item -> {
	    	System.out.println("In thread (E-subscribe): " + Thread.currentThread());
		      System.out.println("Got item " + item);
		    }, err -> {
		      err.printStackTrace();
		    }, () -> {
		    	System.out.println("In thread (E-done): " + Thread.currentThread());
		      System.out.println("Done");
		    });
	}
}
