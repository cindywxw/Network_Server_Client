import java.util.*;
import java.io.*;
import java.net.*;

public class HTTPResponse {
	
	private String respond; //status code
	private Hashtable<String, String> redirTable; //store the filenames in redirect.defs
	private String method; //request method
	private String filename; //file/url
	private String HTTPProtocol; //HTTP protocol
	private Socket socket;
	private InputStream f; // read file content
	private byte[] body = null;
	
	public HTTPResponse( String method, String filename, String HTTPProtocol, Socket socket ) throws IOException {
		
		this.method = method;
		this.filename = filename;
		this.HTTPProtocol = HTTPProtocol;
		this.socket = socket;
		
		if (filename == null || HTTPProtocol == null) {
			resStatus(400);
		} else {
			// check request method
			switch (method) {
			case "GET":
				// check the redirect.defs list
				redirTable = getRedirectList();
				// accessing redirect.defs returns 404 Not Found
				if(filename.equals("/redirect.defs")) {
					resStatus(404);
				} else if (isMoved(redirTable, filename)) {
					// if the file is in the redirect list, return 301
					resStatus(301);
					respond += "Location: " + redirTable.get(filename) + "\r\n\r\n";
				} else {
					filename = "www" + filename;	
					File file = new File(filename);
					// check if the file exists and determine the MIME type
					String mimeType="text/plain";
					if (file.exists() && !file.isDirectory()) {
						if (filename.endsWith(".html") || filename.endsWith(".htm")){
							mimeType="text/html";
						}else if (filename.endsWith(".pdf"))
							mimeType="application/pdf";
						else if (filename.endsWith(".png"))
							mimeType="image/gif";
						else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
							mimeType="image/jpeg";
						else if (filename.endsWith(".pdf"))
							mimeType="application/pdf";
						// if the file exists, return 200 OK and read the contents
						resStatus(200);
						respond += "Content-type: " + mimeType + "\r\n";
						f = new FileInputStream(filename);
						int length = f.available();
						body = new byte[length];
						f.read(body);
						f.close();					
					} else {
						// if the file doesn't exist, return 404 Not Found
						resStatus(404);
					}				
				}
				break;
			case "HEAD":
				// same process as GET, except don't need to return the data
				redirTable = getRedirectList();
				if (isMoved(redirTable, filename)) {
					resStatus(301);
					respond += "Location: " + redirTable.get(filename) + "\r\n";
				} else {
					filename = "www" + filename;
					File file = new File(filename);
					if (file.exists() && !file.isDirectory()) {
						resStatus(200);		
					} else {
						resStatus(404);
					}
				}
				break;
			case "UNKNOWN":
				// POST, DELETE or PUT other requests treated as UNKNOWN and return 403
				resStatus(403); break;
			default:
				// invalid requests return 500
				resStatus(500);
			}
		}
	}
	// set the response status
	public void resStatus (int code) {
		switch (code) {
		case 200:
			respond = "200 OK\r\n"; break;
		case 301:
			respond = "301 Moved Permanently\r\n"; break;
		case 403:
			respond = "403 Forbidden\r\n"; break;
		case 404:
			respond = "404 Not Found\r\n"; break;
		case 500:
			respond = "500 Internal Server Error\r\n"; break;
		default:
			respond = "400 Bad Request\r\n";
		}
	}
	
	// send response
	public void sendResponse(OutputStream os) throws IOException {
		DataOutputStream out = new DataOutputStream(os);

		// header
		String response = HTTPProtocol + " " + respond;
		response += "Connection: close\r\n\r\n";

		// to demonstrate the response of 403 and 500, otherwise not needed
//		if (method.equals("UNKNOWN") || method.equals("INVALID")) {
//			out.writeBytes("\r\n");
//		}
		
		out.writeBytes(response);

		// body
		if (body != null) {
			out.write(body);
		}
		out.flush();
		
	}
	
	// open the redirect.defs file and create a hashtable of redirect list
	Hashtable<String, String> getRedirectList () {
		Hashtable<String, String> redirTable = new Hashtable<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("www/redirect.defs")); 
			String line;

			while((line = br.readLine()) != null) {
				StringTokenizer cat = new StringTokenizer(line);
				String orig = cat.nextToken(); // original site
				String redir = cat.nextToken(); // redirected address
				redirTable.put(orig, redir);
			}
			br.close();
		}
		catch(Exception x) {
			System.out.println("Empty redirect list.");
		}
		return redirTable;
	}
	// check if a site is in the redirect.defs file
	boolean isMoved (Hashtable<String, String> redirTable, String filename){
		boolean redir = false;
		if( redirTable.containsKey(filename)) {
			return true;
		}
		return redir;
	}

}
