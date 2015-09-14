package io.vertex.example.http.simpleform;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class SimpleFormServer extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(SimpleFormServer.class);
	}
	
	@Override
	public void start(){
		final String FILE_LOCATIONS = System.getProperty("user.dir") + "/src/main/java/io/vertex/example/http/simpleform/";
		vertx.createHttpServer().requestHandler(request -> {
			if(request.path().equals("/")){
				request.response().sendFile(FILE_LOCATIONS + "index.html", 
						fileResult -> {
							if(fileResult.failed()){
								System.out.println("failed:" + fileResult.cause().getMessage());
								request.response().setStatusCode(500).end();
							} else {
								//all cool
							}
						});
			} else if(request.path().equals("/form")){
				request.response().setChunked(true);
				
				//Call this with true if you are expecting a multi-part body to be submitted in the request. 
				//This must be called before the body of the request has been received
				//this looks more like a form body parser
				request.setExpectMultipart(true);
				
				request.endHandler((v) -> {
					System.out.println("Form attributes received");
					request.formAttributes().forEach(attribute -> {
						System.out.println(attribute.getKey() + " :: " + attribute.getValue());
						request.response().write("Got attr " + attribute.getKey() + " : " + attribute.getValue() + "\n");
					});
					request.response().end();
				});
				
			} else {
				request.response().setStatusCode(404).end();
			}
		}).listen(8080);
	}
}
