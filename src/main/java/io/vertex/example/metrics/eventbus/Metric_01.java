package io.vertex.example.metrics.eventbus;

import java.util.Random;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;

public class Metric_01 extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Metric_01.class);
	}
	
	@Override
	public void start() throws Exception {
		Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
			    new DropwizardMetricsOptions().setEnabled(true)
			));
		
		EventBus eventBus = vertx.eventBus();
		
		MetricsService metricsService = MetricsService.create(vertx);
		vertx.setPeriodic(5000, t -> {
		      JsonObject metrics = metricsService.getMetricsSnapshot(eventBus);
		      eventBus.publish("metrics", metrics);
		      System.out.println(metrics);
		    });
		
		
		//JsonObject metrics = metricsService.getMetricsSnapshot(eventBus);
		//System.out.println(metrics.getJsonObject("handlers"));
		
		Random random = new Random();
		eventBus.consumer("whatever", msg -> {
	      vertx.setTimer(2000 + random.nextInt(1000), id -> {
	        vertx.eventBus().send("whatever", "hello");
	      });
	    });
	    vertx.eventBus().send("whatever", "hello");
	}
}
