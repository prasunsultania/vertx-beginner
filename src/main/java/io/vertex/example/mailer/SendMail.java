package io.vertex.example.mailer;

import io.vertex.example.util.Runner;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.StartTLSOptions;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.file.AsyncFile;
import io.vertx.rxjava.core.file.FileSystem;
import io.vertx.rxjava.ext.mail.MailClient;

public class SendMail extends AbstractVerticle{
	
	public static void main(String[] args) {
		Runner.runExample(SendMail.class);
	}
	
	@Override
	public void start() throws Exception {
		final String fileName = System.getProperty("user.dir") + "/src/main/conf/configs/config.json";
		FileSystem fileSystem = vertx.fileSystem();
		
		fileSystem.open(fileName, new OpenOptions().setCreate(true), resultRead -> {
			AsyncFile fileRead = resultRead.result();
			fileRead.toObservable().reduce((previousBuffer, currentBuffer) -> {
				return Buffer.buffer(previousBuffer
						.length() + currentBuffer.length()).appendBuffer(previousBuffer)
						.appendBuffer(currentBuffer);
			}).map(data -> {
				return new JsonObject(data.toString("UTF-8"));
			}).flatMap(config -> {
				/*{
				"hostname": "smtp.gmail.com",
				"port": 587,
				"starttls": "REQUIRED",
				"username": "xxx@gmail.com",
				"password": "xxxxxxxxxxxxx"
				}*/
				MailClient mailClient = MailClient.createNonShared(vertx, new MailConfig(config.getJsonObject("mailSettings")));
				return mailClient.sendMailObservable(new MailMessage().
						setTo(config.getString("receiver")).
						setSubject("Howdy").
						setFrom(config.getJsonObject("mailSettings").getString("username")).
						setText("Hey, whats up?"));
			}).subscribe(mailSent -> System.out.println("Mail has been sent"),
					err -> System.out.println("Error sending mail: " + err.getMessage()),
					() -> System.exit(0)
			);
		});
	}
}
