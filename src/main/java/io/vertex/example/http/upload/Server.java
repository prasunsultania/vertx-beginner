package io.vertex.example.http.upload;

import java.util.UUID;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pump;

public class Server extends AbstractVerticle {

	// Convenience method so you can run it in your IDE
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}

	final String FILE_LOCATIONS = "/Users/prasunsultania/Documents/workspace/vertex/experimental/maven-core/src/main/java/io/vertex/example/http/upload/uploadeddata/";

	@Override
	public void start() {
		vertx.createHttpServer().requestHandler(request -> {
			//open file and enable pumping first, it pauses data for handler, but, the framework may still buffer the data
			request.pause();
			
			String fileName = FILE_LOCATIONS + UUID.randomUUID() + ".uploaded";

			vertx.fileSystem().open(fileName, new OpenOptions(), ares -> {
				
				if(ares.failed()){
					//TODO handle failure
					request.response().setStatusCode(500).end();
					return;
				} 
				
				//get result of opening file system
				AsyncFile file = ares.result();
				
				//pump will handle back pressure
				Pump pump = Pump.pump(request, file);
				
				//On response end close the file
				request.endHandler(v1->{
					file.close(v2 -> {
						if(v2.failed()){
							System.out.println("File close failed :" + 
									v2.cause().getMessage());
							request.response().setStatusCode(500).end();
						} else {
							System.out.println("Uploaded to : " + fileName);
							request.response().end();
						}
					});
				});
				
				//initiate pumping (streams from readstream to write stream and handles flow control)
				pump.start();
				
				request.resume();
				
			});
		}).listen(8080);
	}
}
