package server;

import java.io.*;
import java.net.*;
import server.handlers.DownloadBatchHandler;
import server.handlers.DownloadFileHandler;
import server.handlers.GetFieldsHandler;
import server.handlers.GetProjectsHandler;
import server.handlers.GetSampleImageHandler;
import server.handlers.SearchHandler;
import server.handlers.SubmitBatchHandler;
import server.handlers.ValidateUserHandler;
import com.sun.net.httpserver.*;

public class Server {

	private static final int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;

	private HttpServer server;
	
	private Server() {
		return;
	}
	
	private void run(String serverPortNumber) {
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerException e) {
			return;
		}
		
		try {
			if(serverPortNumber == "")
				server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											                      MAX_WAITING_CONNECTIONS);
			else
				server = HttpServer.create(new InetSocketAddress(
						         Integer.valueOf(serverPortNumber)),MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {	
			return;
		}

		server.setExecutor(null); 
		
		server.createContext("/ValidateUser", validateUserHandler);
		server.createContext("/GetProjects", getProjectsHandler);
		server.createContext("/GetSampleImage", getSampleImageHandler);
		server.createContext("/DownloadBatch", downloadBatchHandler);
		server.createContext("/SubmitBatch", submitBatchHandler);
		server.createContext("/GetFields", getFieldsHandler);
		server.createContext("/Search", searchHandler);
		server.createContext("/", downloadFileHandler);

		server.start();
	}

	private HttpHandler validateUserHandler = new ValidateUserHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler getSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler submitBatchHandler = new SubmitBatchHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler searchHandler = new SearchHandler();
	private HttpHandler downloadFileHandler = new DownloadFileHandler();
	
	public static void main(String[] args) {
		new Server().run(args[0]);
	}

}
