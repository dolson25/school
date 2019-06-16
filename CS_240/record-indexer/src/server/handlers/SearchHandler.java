package server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.ServerException;
import server.ServerFacade;
import shared.communication.SearchInput;
import shared.communication.SearchOutput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("contactmanager"); 
	
	private XStream xmlStream = new XStream(new DomDriver());	
	

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		SearchInput params = (SearchInput)xmlStream.fromXML(exchange.getRequestBody());
		SearchOutput result;
		
		try {
			result = ServerFacade.search(params);
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}
