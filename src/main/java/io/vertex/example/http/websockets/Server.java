package io.vertex.example.http.websockets;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;

public class Server extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(Server.class);
	}
	
	@Override
	public void start(){
		final String FILE_LOCATIONS = System.getProperty("user.dir") + "/src/main/java/io/vertex/example/http/websockets/";
		vertx.createHttpServer().websocketHandler(
				ws -> ws.handler(data -> {
					System.out.println(data.toString("ISO-8859-1"));
					//Echo the data
					//we would write back as binary
					ws.writeBinaryMessage(data);
				})).
				requestHandler(request -> {
					if(request.uri().equals("/")){
						request.response().sendFile(FILE_LOCATIONS + "ws.html");
					}
				}).listen(8080);
	}
}
