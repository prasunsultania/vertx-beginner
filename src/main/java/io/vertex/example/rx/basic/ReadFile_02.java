package io.vertex.example.rx.basic;

import io.vertex.example.util.Runner;
import io.vertx.core.file.OpenOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileSystem;

/**
 * using Rxified vert.x APIs
 * observables are hot observables, i.e they will produce notifications regardless of subscriptions
 * @author prasunsultania
 *
 */
public class ReadFile_02 extends AbstractVerticle {
	
	public static void main(String[] args) {
		Runner.runExample(ReadFile_02.class);
	}
	
	@Override
	public void start() throws Exception {
		final String fileName = System.getProperty("user.dir") + "/src/main/java/io/vertex/example/rx/basic/heroku_sys.txt";
		FileSystem fileSystem = vertx.fileSystem();
		
		fileSystem.open(fileName, new OpenOptions(), result -> {  
			AsyncFile file = result.result();
			  file.toObservable().map(data -> data.toString("UTF-8").toUpperCase())
			  .forEach(data -> System.out.println("Read data: " + data.toString()),
					  error -> error.printStackTrace());
		});
	}
}
