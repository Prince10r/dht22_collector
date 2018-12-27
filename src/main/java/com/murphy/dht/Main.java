package com.murphy.dht;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.dht.dht.schedule.DHTCollector;
import com.murphy.jersey.filter.CORSFilter;

public class Main {

	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final String BASE_URI = "http://0.0.0.0:8080/";
	
	private DHTCollector dhtCollector;

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in com.example.rest package
		final ResourceConfig rc = new ResourceConfig().packages("com.murphy.dht.dht.rest");
		rc.register(JacksonFeature.class);
		rc.register(new CORSFilter());
		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

		CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(Main.class.getClassLoader(), "www/static/");
		staticHttpHandler.setFileCacheEnabled(false);
		
		httpServer.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");
		return httpServer;
	}
	
	public void startDHTCollector() {
		dhtCollector = new DHTCollector();
		dhtCollector.start();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		logger.info("Starting DHT Manager");
		long startTime = System.currentTimeMillis();

		Main main = new Main();
		final HttpServer server = main.startServer();
		main.startDHTCollector();
		long endTime = System.currentTimeMillis();
		logger.info("Time to start up: " + (endTime - startTime) + " ms");
		logger.info("Webpage found at http://0.0.0.0:8080");
		
	}
	
}
