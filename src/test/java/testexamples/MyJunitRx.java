package testexamples;

import io.vertex.example.unit.SomeVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpServer;



//import io.vertx.rxjava.ext.unit.TestContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MyJunitRx {
	Vertx vertx;
	HttpServer server;
	
	@Before
	public void before(TestContext testContext){
		Async async = testContext.async();
		System.out.println("Before");
		vertx = Vertx.vertx();
		server = vertx.createHttpServer();
		
		server.requestStream().toObservable().
				subscribe(req -> req.response().end("foo"),
						err -> testContext.assertFalse(true, "Server Should be able to handle request"));
		
		server.listenObservable(8080).subscribe(res->async.complete(),
				err -> testContext.assertFalse(true, "Server Should be listening"));
	}
	
	@After
	public void after(TestContext context) {
		System.out.println("After");
		Async async = context.async();
		vertx.closeObservable().subscribe(res->async.complete(),
				//fail on error
				err -> context.assertFalse(true, "Vertx Should be closed"));
	}
	
	@Test
	public void test1(TestContext context) {
		
		//start async test
	    Async async = context.async();
		 // Send a request and get a response
	    HttpClient client = vertx.createHttpClient();
	    
	    HttpClientRequest request = client.request(HttpMethod.GET, 8080, "localhost", "/");
	    
	    request.toObservable().flatMap(response-> { 
	    	context.assertEquals(response.statusCode(), 200); return response.toObservable();})
	    .subscribe(data->{context.assertEquals(data.toString("UTF-8"), "foo"); async.complete();},
	    		err->context.assertFalse(true, "server should respond"));
	    
	    request.end();
	}
	
	@Test
	  public void test2(TestContext context) {
		//start async test
	    Async async = context.async();
	    
	    // Deploy and undeploy a verticle
		vertx.deployVerticleObservable(SomeVerticle.class.getName())
		.flatMap(deploymentID-> vertx.undeployObservable(deploymentID/* + 6*/))
		.subscribe(res -> async.complete(), err->context.assertFalse(true, "Deploy/Undeploy failed:" + err.getMessage()));
	    
	  }
}
