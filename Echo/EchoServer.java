//package network;

import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer --port=<port number>");
            System.exit(1);
        }
        
        ServerSocket serverSocket = null;
        String a = args[0];
        String port = a.replace("--port=", "");
        int portNumber = Integer.parseInt(port);
//        System.out.println(portNumber);
        
        try { 
            serverSocket = new ServerSocket(portNumber); 
//            System.out.println("Connected successfully!1");
        } catch (IOException e) { 
            System.err.println("Could not listen on port: " + portNumber); 
            System.exit(1); 
        }
        
        Socket clientSocket = null;
        System.out.println ("Waiting for connection...");
        
        PrintWriter out = null;
        BufferedReader in = null;
        
        try {
            clientSocket = serverSocket.accept();     
            out = new PrintWriter(clientSocket.getOutputStream(), true); 
//            System.out.println("Connected successfully!2");
            in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
//            System.out.println("Connected successfully!3");
 
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
        System.out.println("Connected successfully!");
        System.out.println ("Waiting for input...");
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println ("Server: " + inputLine); 
            out.println(inputLine);
        }
        
        System.out.println ("Connection closed.");
        out.close(); 
        in.close(); 
        clientSocket.close(); 
        serverSocket.close(); 
    }
}

