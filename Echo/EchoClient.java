//package network;

import java.io.*;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        
        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient --serverIP=<host name> --serverPort=<port number>");
            System.exit(1);
        }
        String a = args[0];
        String b = args[1];
        String hostName = a.replace("--serverIP=","");
        String port = b.replace("--serverPort=", "");
        int portNumber = Integer.parseInt(port);
//        System.out.println(hostName + portNumber);;
        
        System.out.println ("Attemping to connect to host " +
        		hostName + " on port " + portNumber);
        
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(hostName, portNumber);
//            System.out.println("Connected successfully!1");
            out = new PrintWriter(echoSocket.getOutputStream(), true);
//            System.out.println("Connected successfully!2");
            in = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
//            System.out.println("Connected successfully!3");
           
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.err.println("Please start the Master first");
            System.exit(1);
        }         
        
        System.out.println("Connected successfully!");
        BufferedReader stdIn = new BufferedReader(
                    			new InputStreamReader(System.in));
        
        String userInput;
        System.out.print ("input: ");
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine());
            System.out.print ("input: ");

        }
        
        out.close();
    	in.close();
    	stdIn.close();
    	echoSocket.close();
        
    }
}


