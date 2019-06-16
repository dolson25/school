package server.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DownloadFileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {

		String s = "." + exchange.getRequestURI().toString();
		
		java.nio.file.Path p = Paths.get(s);
		
		byte[] b = Files.readAllBytes(p);
		
		exchange.sendResponseHeaders(200, b.length);
		exchange.getResponseBody().write(b);
		exchange.getResponseBody().close();
	}

}
