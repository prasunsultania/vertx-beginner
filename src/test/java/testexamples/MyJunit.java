package testexamples;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertex.example.unit.SomeVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MyJunit {
	
	Vertx vertx;
	HttpServer server;
	
	@Before
	public void before(TestContext testContext){
		System.out.println("Before");
		vertx = Vertx.vertx();
		server = vertx.createHttpServer().requestHandler(req -> req.response().end("foo")).
				listen(8080, testContext.asyncAssertSuccess());
	}
	
	@After
	public void after(TestContext context) {
		System.out.println("After");
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void test1(TestContext context) {
		 // Send a request and get a response
	    HttpClient client = vertx.createHttpClient();
	    
	    //start async test
	    Async async = context.async();
	    client.getNow(8080, "localhost", "/", resp -> {
	      resp.bodyHandler(body -> {
	        context.assertEquals("foo", body.toString());
	        client.close();
	        //mark test as complete as it is an async one
	        async.complete();
	      });
	    });
	}
	
	@Test
	  public void test2(TestContext context) {
	    // Deploy and undeploy a verticle
	    vertx.deployVerticle(SomeVerticle.class.getName(), context.asyncAssertSuccess(deploymentID -> {
	      vertx.undeploy(deploymentID, context.asyncAssertSuccess());
	    }));
	  }
	
}
