package io.vertex.example.rx.scheduler.timer;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import io.vertex.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;

public class Periodic extends AbstractVerticle{
	
	public static void main(String[] args) {
	    Runner.runExample(Periodic.class);
	  }
	
	@Override
	public void start() throws Exception {
		
		//When working with scheduler it is important to understand where exactly your tasks are running
		//and where your events are being listened
		
		System.out.println("In thread: " + Thread.currentThread());
		
		//In below code, both the timer and listener event will run in Rx thread and NOT in vertx thread
		Observable<Long> o = Observable.timer(0, 2500, TimeUnit.MILLISECONDS);
		o.subscribe(item -> System.out.println("Got item: " + item + " In thread: " + Thread.currentThread()));
		
		// Create a scheduler for a Vertx object, actions are executed on the event loop.
		//timer do not execute on event loop, and worker thread
		//but we do want to perform action on event loop and listen to event in vertx event loop thread
		//use vertx scheduler, actions will execute on event loop
		Scheduler scheduler = RxHelper.scheduler(vertx);
		Observable.timer(0, 3000, TimeUnit.MILLISECONDS, scheduler).subscribe(
				item -> System.out.println("Got item: " + item + " In thread: " + Thread.currentThread()));
	}

}
