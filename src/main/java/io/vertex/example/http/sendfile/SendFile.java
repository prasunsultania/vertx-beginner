package io.vertex.example.http.sendfile;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class SendFile extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(SendFile.class);
	}
	
	@Override
	public void start(){
		final String FILE_LOCATIONS = System.getProperty("user.dir") + "/src/main/java/io/vertex/example/http/sendfile/";
		vertx.createHttpServer().requestHandler(request -> {
			String fileName = null;
			if(request.path().equals("/")){
				fileName = "index.html";
			} else if(request.path().equals("/page1.html")){
				fileName = "page1.html";
			} else if(request.path().equals("/page2.html")){
				fileName = "page2.html";
			} else {
				request.response().setStatusCode(404).end();;
			}
			
			
			if(fileName != null){
				//sendFile method sends file
				//Ask the OS to stream a file as specified by filename 
				//directly from disk to the outgoing connection, 
				//bypassing userspace altogether 
				//(where supported by the underlying operating system.
				request.response().sendFile(FILE_LOCATIONS + fileName, fileAsyncStatus ->{
					
					if(fileAsyncStatus.failed()){
						System.out.println("failed:" + fileAsyncStatus.cause().getMessage());
						request.response().setStatusCode(500).end();
					} else{
						System.out.println("file sent");
						System.out.println(fileAsyncStatus.result());
					}
					
				});
			}
			
		}).listen(8080);
	
	}
	
}
