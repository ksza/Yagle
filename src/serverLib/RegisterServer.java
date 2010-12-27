package serverLib;

import java.io.*;
import java.net.*;
import java.util.*;

public class RegisterServer {
        // The ServerSocket we'll use for accepting new connections
	private ServerSocket ss;	
        
	public RegisterServer(int port) throws IOException {
            // All we have to do is listen
            listen(port);
	}
	
	private void listen(int port) throws IOException {
            // Create the ServerSocket
            ss = new ServerSocket(port);            
            System.out.println( "Listening on " + ss );
            // Keep accepting connections forever
            while (true) {
		// Grab the next incoming connection
		Socket s = ss.accept();		
		System.out.println( "Connection from " + s );
		// Get the USER name form the client and create a DataOututStream & DataInputStream for it
                DataInputStream din = new DataInputStream(s.getInputStream());               
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());             		
                	
                new RegisterServerThread(this, s);
            }
        }          
        
	// main routine	
	static public void main(String args[]) throws Exception {            
            int port = 4444;
            // Create a RegisterServer object, which will automatically begin accepting connections.
            new RegisterServer(port);
	} 
}