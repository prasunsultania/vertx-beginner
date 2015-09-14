package io.vertex.example.backpressureandstreams.pumpfile;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.streams.Pump;

public class ReadFile extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(ReadFile.class);
	}
	
	@Override
	public void start() throws Exception {
		//A large file:  363.7 MB file taken for this test
		final String fileName = "/Users/prasunsultania/Desktop/heroku_sys.txt";
		final String fileNameWrite = "/Users/prasunsultania/Desktop/write.txt";
		
		FileSystem fileSystem = vertx.fileSystem();
		
		fileSystem.open(fileNameWrite, new OpenOptions().setCreate(true), resultWrite -> {
			AsyncFile fileWrite = resultWrite.result();
			
			fileSystem.open(fileName, new OpenOptions(), resultRead -> {
				AsyncFile fileRead = resultRead.result();
				
				Pump pump = Pump.pump(fileRead, fileWrite);
				
				fileRead.endHandler(done-> {
					System.out.println("File has been pumped from reader");
					
					//this is the context of outer class
					vertx.undeploy(this.deploymentID(), result -> {
						if(result.failed()){
							System.out.println(result.cause().getMessage());
						} else {
							System.out.println("Undeployed verticle");
							System.exit(0);
						}
					});
				});
				
				System.out.println("Initiating pumping ");
				//initiate pumping (streams from readstream to write stream and handles flow control/back-pressure)
				pump.start();
			});
		});
	}
}
