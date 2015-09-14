package io.vertex.example.rx.basic;

import rx.Observable;
import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.rx.java.RxHelper;
import io.vertx.core.buffer.Buffer;

public class ReadFile_01 extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(ReadFile_01.class);
	}
	
	@Override
	public void start() throws Exception {
		final String fileName = System.getProperty("user.dir") + "/src/main/java/io/vertex/example/rx/basic/heroku_sys.txt";
		FileSystem fileSystem = vertx.fileSystem();
		
		//method1 using RxHelper
		fileSystem.open(fileName, new OpenOptions(), result -> {
		  AsyncFile file = result.result();
		  Observable<Buffer> observable = RxHelper.toObservable(file);
		  observable.forEach(data -> System.out.println("Read data: " + data.toString("UTF-8")),
				  error -> error.printStackTrace());
		});
		
	}
}
