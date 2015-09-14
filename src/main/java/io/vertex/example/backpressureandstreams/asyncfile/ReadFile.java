package io.vertex.example.backpressureandstreams.asyncfile;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;

/**
 * This code examples is to show handling Back pressure manually
 * We are copying a large file to an another file
 * Reading is generally faster than write, and it is likely, that 
 * @author prasunsultania
 *
 */
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
		
		Counter count = new Counter();
		Counter totalCount = new Counter();
		
		
		fileSystem.open(fileNameWrite, new OpenOptions().setCreate(true), resultWrite -> {
			AsyncFile fileWrite = resultWrite.result();
			
			fileSystem.open(fileName, new OpenOptions(), resultRead -> {
				AsyncFile fileRead = resultRead.result();
				
				fileRead.handler(buffer -> {
					count.increment();
					totalCount.increment();
					fileWrite.write(buffer);
					
					//check if reader is out of capacity for taking more data
					if(fileWrite.writeQueueFull()){
						System.out.println("writing queue full, taking a pause... :" + count.getCount());
						count.reset();
						
						//this pauses file handler from being called
						fileRead.pause();
						
						//Once, the writer is ready to accept more data, let it take up more
						fileWrite.drainHandler(done -> {
							System.out.println("write queue drained, so resuming now...");
							
							//allow file reader handler to resume so, that it accepts incoming data 
							fileRead.resume();
						});
					}
				});
				
				fileRead.endHandler(done -> {
					System.out.println("End handler encountered, closing files now : " + totalCount.getCount());
					fileRead.close();
					fileWrite.close();
					vertx.undeploy(this.deploymentID(), res -> {
						if(res.failed()){
							System.out.println(res.cause().getMessage());
						} else{
							System.out.println("Undeployed");
						}
						System.exit(0);
					});
				});
			});
		});
		
		
	}
}
