package serverLib;

import java.io.*;
import java.net.*;
import java.util.*;

public class PlayServer {
        // The ServerSocket we'll use for accepting new connections
	private ServerSocket ss;
	// A mapping from USERS to DataOutputStreams. This will
	// help us avoid having to create a DataOutputStream each time
	// we want to write to a USER.        
	protected Hashtable outputStreams = new Hashtable();
        // a mapping from USERS to sockets [connections to clients]
        protected Hashtable socketMap = new Hashtable();
        // A mapping from USERS to gameNames
        protected Hashtable gameRooms = new Hashtable();
        protected Vector<String> chalengedUsers = new Vector<String>(), chalengers = new Vector<String>();
        protected Vector<String> playingUsers = new Vector<String>();
        
	public PlayServer(int port) throws IOException {
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
                
                // get the new client's user name -> "1 userName"
                String userName = din.readUTF();                
                StringTokenizer st = new StringTokenizer(userName);                
                if(st.hasMoreTokens()) 
                    if(st.nextToken().equals("1"))
                        userName = st.nextToken();                        
                            
                // show it
                System.out.println("New user: " + userName);                          
                		
                // Save this stream so we don't need to create it again
                outputStreams.put(userName, dout);
                socketMap.put(userName, s);                           
                
                // Create a new thread for this connection, and then forget about it		
                new PlayServerThread(this, s, userName);
            }
        }        
	            
	// Send a message to a USER (utility routine)
	protected void sendMessage(String userName, String message) {
            // We synchronize on this because another thread might be calling 
            // another method that uses the same object
            synchronized(outputStreams) {			
                DataOutputStream dout = (DataOutputStream)outputStreams.get(userName);			
                // send the message
                try {
                    dout.writeUTF(message);
                } catch(IOException ie) { System.out.println( ie ); }
            }
        }	
       
	// main routine	
	static public void main(String args[]) throws Exception {            
            int port = 4445;
            // Create a PlayServer object, which will automatically begin accepting connections.
            new PlayServer(port);
	}     
}