package io.vertex.example.jsonconfig;

import io.vertex.example.util.Runner;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileSystem;


public class JsonParser extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(JsonParser.class);
	}

	@Override
	public void start() throws Exception {
		String fileName = System.getProperty("user.dir") + "/src/main/conf/config.json";

		FileSystem fileSystem = vertx.fileSystem();

		fileSystem.open(
				fileName,
				new OpenOptions(),
				result -> {
					AsyncFile file = result.result();
					file.toObservable().reduce(
							(previousBuffer, currentBuffer) -> {
								if(previousBuffer == null || currentBuffer == null){
									System.out.println("null buffer");
								}
								Buffer buf = Buffer.buffer(previousBuffer
										.length() + currentBuffer.length());
								return buf.appendBuffer(previousBuffer)
										.appendBuffer(currentBuffer);
							}).subscribe(data-> {
								JsonObject json = new JsonObject(data.toString("UTF-8"));
								System.out.println(json.toString());
								System.out.println(json.getJsonObject("a").getJsonArray("c"));
								System.out.println(json.getJsonObject("a").getJsonArray("d"));
							}, err -> {
								System.out.println("error: " + err.getMessage());
								vertx.undeploy(this.deploymentID());
								System.exit(0);
							}, () ->{
								vertx.undeploy(this.deploymentID());
								System.exit(0);
							});
				});
	}
}
