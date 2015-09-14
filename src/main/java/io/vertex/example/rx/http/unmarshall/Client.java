package io.vertex.example.rx.http.unmarshall;

import io.vertx.core.http.HttpMethod;
import io.vertex.example.util.Runner;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.core.http.HttpClientResponse;

/**
 * Run this example with Server of io.vertex.example.rx.http.zip 
 * @author prasunsultania
 *
 */
public class Client extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(Client.class);
  }

  // Unmarshalled response from server
  static class Data {
    public Long time;
  }

  @Override
  public void start() throws Exception {
    HttpClient client = vertx.createHttpClient();

    HttpClientRequest req = client.request(HttpMethod.GET, 8080, "localhost", "/");
    req.toObservable().
        flatMap(HttpClientResponse::toObservable).
        
        //Lift operator is a hook for custom operator
        //Unmarshaller is a RxHelper util of vertx that collects all json data and then marshalls it
        // Unmarshall(convert to Java Object) the response to the Data object via Jackon
        lift(io.vertx.rxjava.core.RxHelper.unmarshaller(Data.class)).

        subscribe(
            data -> {
              System.out.println("Got response " + data.time);
            });

    // End request
    req.end();
  }
}