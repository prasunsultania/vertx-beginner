package io.vertex.example.rx.http.reduce;

import io.vertex.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class Server extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Server.class);
  }

  @Override
  public void start() throws Exception {
    HttpServer server = vertx.createHttpServer();
   
    server.requestStream().toObservable().subscribe(request -> {
    	request.response().setChunked(true).putHeader("content-type", "PLAINTEXT");
    	request.response().write("hey 1").write("hey 2").write("hey 3").end();
      //req.response().putHeader("content-type", "text/html").end("<html><body><h1>Hello from vert.x!</h1>" + body2 +"</body></html>");
    });
    server.listenObservable(8080).subscribe(
    		res->System.out.println("listening"), e->System.out.println("falied to listen: " + e.getMessage()));
  }
}
