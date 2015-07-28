package io.vertex.example.http.simpleformupload;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class SimpleFormUploadServer extends AbstractVerticle{
	
	// Convenience method so you can run it in your IDE
	  public static void main(String[] args) {
	    Runner.runExample(SimpleFormUploadServer.class);
	  }
	  
	  @Override
	  public void start(){
		  vertx.createHttpServer().requestHandler(request -> {
			  if(request.uri().equals("/")){
				  request.response().sendFile("index.html", fileResult -> {
					 if(fileResult.failed()){
						 System.out.println("File integration failed: " + 
								 fileResult.cause().getMessage());
						 request.response().setStatusCode(500).end();
					 }
				  });
			  }
		  }).listen(8080);
	  }
	  
}
