import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.Runnable;
import java.lang.Thread;


// A WebServer waits for clients to connect, then starts a separate thread to handle the HTTP requests.
public class WebServer {
	
	public static void main(String[] args) {
//		int portNumber = 1234;
		// use command line to get the port number
		if (args.length != 1) {
			System.err.println("Usage: java WebServer --port=<port number> ( should > 1024)");
			System.exit(1);
		}  
		String a = args[0];
		String port = a.replace("--port=", "");
		int portNumber = Integer.parseInt(port);

		if (portNumber <= 1024) {
			System.err.println("Usage: java WebServer --port=<port number> ( should > 1024)" );
			System.exit(1);
		}
		System.out.println ("Waiting for connection...");
		
		try {	
			// Open connections to the socket
			ServerSocket socket = new ServerSocket(portNumber);
			System.out.println ("Connect to the server listening on port " + portNumber);

			while (true) {
				Socket client = socket.accept();
				// Handle the client in a separate thread
				new Thread(new ClientHandler(client)).start(); 
			}
		}
		catch (Exception e) {
			System.err.println("Exception caught:" + e);
		}
	}
}


