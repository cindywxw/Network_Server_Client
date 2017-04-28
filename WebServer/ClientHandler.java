import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.Runnable;


// A ClientHandler reads an HTTP request and responds
public class ClientHandler implements Runnable {
	private Socket socket;  // The accepted socket from the WebServer
	private String method;  // the request method
	private String filename;  // file/url
	private String HTTPProtocol; // HTTP protocol
	private BufferedReader in;  
//	private PrintStream out;
	private OutputStream out; 
	
	
	public ClientHandler(Socket socket) throws Exception {
		this.socket = socket;
	}

	// Read the HTTP request, respond, and close the connection
	public void run() {

		try {		
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = socket.getOutputStream();
	
			// Parse the HTTP request from first input line, like "GET /filename.html HTTP/1.1".		
			String s = in.readLine();
			
			try {
				String[] sp = s.split("\\s+");
				if (sp[0].equalsIgnoreCase("GET"))
					method = "GET";
				else if (sp[0].equalsIgnoreCase("HEAD"))
					method = "HEAD";
				else
					method = "UNKNOWN";
			} catch (Exception e) {
				method = "UNKNOWN";
			}
			
			// parse url
			try {
				String[] sp = s.split("\\s+");
				filename = sp[1];
//				System.out.println("fname "+filename);
				if ( filename.startsWith("https")) {
					method = "UNKNOWN";
				}
				HTTPProtocol= sp[2];
			} catch (Exception e) {
				method = "INVALID";
				
			}
//			while(!(s = in.readLine()).equals(""))  {
//				System.out.println(s);
//			}
//			in.close();

			HTTPResponse res = new HTTPResponse(method,filename, HTTPProtocol, socket);
			res.sendResponse(out);
			res.sendResponse(System.out);
			out.close();
			System.out.println("Close connection");
//			socket.close();
		}
		catch (Exception e) {
			e.printStackTrace(); 
			System.out.println("Exception caught: client disconnected.");
		}
		finally {
			try { socket.close(); }
		      catch (Exception e ){ ; }
		}

	}
	
}
