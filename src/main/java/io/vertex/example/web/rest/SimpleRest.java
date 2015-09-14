package io.vertex.example.web.rest;

import io.vertex.example.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.Map;
import java.util.HashMap;

/**
 * Aims at writing a basic REST service and also setting logging properties 
 * @author prasunsultania
 *
 */
public class SimpleRest extends AbstractVerticle{
	
	//Test also the logging properties
	static{
		System.setProperty("java.util.logging.config.file", System.getProperty("user.dir") + "/src/main/resources/vertx-default-jul-logging.properties");
	}
	
	Logger logger = LoggerFactory.getLogger(SimpleRest.class);
	
	private Map<String, JsonObject> products = new HashMap<>();
	
	public static void main(String[] args) {
		Runner.runExample(SimpleRest.class);
	}
	
	@Override
	public void start() throws Exception {
		setUpInitialData();
		
		Router router = Router.router(vertx);
		
		router.route().handler(BodyHandler.create());
		router.get("/products/:productID").handler(this::handleGetProduct);
		router.get("/products").handler(this::handleListProducts);
		router.put("/products/:productID").handler(this::handleAddProduct);
		router.delete("/products/:productID").handler(this::deleteProduct);
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
	
	private void deleteProduct(RoutingContext routingContext){
		String productID = routingContext.request().getParam("productID");
		HttpServerResponse response = routingContext.response();
		
		if(productID == null){
			sendError(400, response);
		} else {
			if(products.containsKey(productID)){
				products.remove(productID);
				response.end();
			} else {
				sendError(404, response);
			}
			
		}
	}
	
	private void handleAddProduct(RoutingContext routingContext) {
		String productID = routingContext.request().getParam("productID");
		HttpServerResponse response = routingContext.response();
		
		if(productID == null){
			sendError(400, response);
		} else {
			JsonObject productBody = routingContext.getBodyAsJson();
			if(productBody == null){
				sendError(400, response);
			} else {
				products.put(productID, productBody);
				response.end();
			}
		}
	}
	
	private void handleGetProduct(RoutingContext routingContext) {
		String productID = routingContext.request().getParam("productID");
		HttpServerResponse response = routingContext.response();
		
		if(productID == null){
			sendError(400, response);
		} else {
			JsonObject product = products.get(productID);
			if(product == null){
				sendError(404, response);
			} else {
				response.putHeader("content-type", "application/json").end(product.encodePrettily());
			}
		}
	}
	
	private void handleListProducts(RoutingContext routingContext) {
		JsonArray jsonArray = new JsonArray();
		products.forEach((k, v) -> jsonArray.add(v));
		routingContext.response().putHeader("content-type", "application/json").end(jsonArray.encodePrettily());
	}
	
	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}
	
	private void setUpInitialData() {
		System.out.println(getClass().getClassLoader().getResource("logging.properties"));
		System.out.println(SimpleRest.class.getClassLoader().getResource("logging.properties"));
		//logger.
		addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
		addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
		addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
		logger.info("initial setup done");
		logger.error("oops!");
	}
	
	private void addProduct(JsonObject product) {
		products.put(product.getString("id"), product);
	}
}
