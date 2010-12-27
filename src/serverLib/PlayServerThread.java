package serverLib;

import java.io.*;
import java.net.*;
import java.util.*;

public class PlayServerThread extends Thread {
    // The PlayServer that spawned us
    private PlayServer server;
    // The Socket connected to our client
    private Socket socket;
    // this client's userName
    String userName, currentGame;
    
    // constructor
    public PlayServerThread(PlayServer server, Socket socket, String userName) {
        // Save the parameters
        this.server = server;
        this.socket = socket;
        this.userName = userName;
        // Start up the thread
        start();
    }
    
    // Send a message to all users in a gameRoom
    private void sendToAll(String message, String gameRoom) {
        // We synchronize on this because another thread might be calling
        // another method that uses the same object
        System.out.println("##$$ " + message + " .. " + gameRoom);
        synchronized(server.gameRooms) {
            // For each client ...
            int i = 0;
            for (Enumeration e = server.gameRooms.keys(); e.hasMoreElements(); ) {
                // ... get the output stream ...
                String user = (String)e.nextElement();
                
                if(((String)server.gameRooms.get(user)).equals(gameRoom)) {
                    DataOutputStream dout = (DataOutputStream)server.outputStreams.get(user);
                    // ... and send the message
                    try {
                        System.out.println("##$$ " + i); i++;
                        dout.writeUTF(message);
                    } catch( IOException ie ) { System.out.println( ie ); }
                }
            }
        }
    }
    
    private void sendMessage(String userName, String message) {
        try {
            synchronized(server.outputStreams) {
                DataOutputStream auxDout = (DataOutputStream)server.outputStreams.get(userName);
                auxDout.writeUTF(message);
            }
        } catch(IOException e) { e.printStackTrace(); }   
    }
    
    // This runs in a separate thread when start() is called in the constructor.
    public void run() {
        try {
            // Create a DataInputStream for communication
            DataInputStream din = new DataInputStream(socket.getInputStream());
            
            // send the games from the server
            File dir = new File("SGames");
                String dirList = "1 ";
                if(dir.isDirectory()) {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++) {                        
                        dirList += children[i] + " ";                         
                    }
                }
                server.sendMessage(userName, dirList);
                
            // Over and over
            while (true) {
                String message = null;
                // read the next message
                message = din.readUTF();
                
                // parse the message
                StringTokenizer st = new StringTokenizer(message);
                if(st.hasMoreTokens())
                    switch(Integer.parseInt(st.nextToken())) {
                        case 2: { // Change game / gameRoom
                            String gameName = st.nextToken();
                            System.out.println("--->> " + gameName);
                            synchronized(server.gameRooms) {
                                if(! server.gameRooms.containsKey(userName)) {
                                    this.sendToAll("3 " + userName, gameName);
                                    this.sendToAll("4 " + "User " + userName + " has entered gameRoom", gameName);
                                    server.gameRooms.put(userName, gameName);
                                } else {
                                    System.out.println("--->> Change");
                                    server.gameRooms.remove(userName);
                                    this.sendToAll("2 " + userName, currentGame);
                                    this.sendToAll("4 " + "User " + userName + " has left gameRoom", currentGame);
                                    this.sendToAll("3 " + userName, gameName);
                                    this.sendToAll("4 " + "User " + userName + " has entered gameRoom", gameName);
                                    server.gameRooms.put(userName, gameName);
                                }
                                
                                currentGame = gameName;
                                
                                synchronized(server.gameRooms) {
                                    // send the already connected clients userNames  to the new user in the new gameRoom
                                    Enumeration e = server.gameRooms.keys();
                                    try {
                                        DataOutputStream auxDout = (DataOutputStream)server.outputStreams.get(userName);
                                        auxDout.writeUTF("4 " + "You have entered the " + currentGame + " gameRoom");
                                        while(e.hasMoreElements()) {                                            
                                            // "3 userName"
                                            String user = (String)e.nextElement();
                                            
                                            System.out.println("%% " + user);
                                            if(((String)server.gameRooms.get(user)).equals(currentGame)) {
                                                auxDout.writeUTF("3 " + user);
                                            }
                                        }                                        
                                    } catch(IOException ie) { System.out.println( ie ); }
                                }
                            }
                            
                            break; }
                        
                        case 3: { // User message: 3 <<userName>> <<message>>
                            String auxMessage = "5 " + userName + " ";
                            while(st.hasMoreTokens()) auxMessage += st.nextToken() + " ";
                            System.out.println("^^^^^ " + auxMessage);
                            this.sendToAll(auxMessage, currentGame);
                            break; }
                        
                        case 4: { // User chalenges: 4 <<chalengedUser>>
                            // chalengerUser -> userName
                            String chalengedUser = st.nextToken();
                            System.out.println("$$ Chalengers: " + server.chalengers + " $$ Chalenged: " + server.chalengedUsers);
                            synchronized(server.chalengers) {
                                if(! server.chalengers.contains(userName)) {
                                    synchronized(server.chalengedUsers) {
                                        if(! server.chalengedUsers.contains(chalengedUser)) {
                                            synchronized(server.playingUsers) {
                                                if(server.playingUsers.contains(userName)) {
                                                    this.sendMessage(userName, "4 " + "You are already playing a game");
                                                } else if(server.playingUsers.contains(chalengedUser)) {
                                                    this.sendMessage(userName, "4 " + "User " + chalengedUser + " is already playing another game");
                                                } else {
                                                    server.chalengers.add(userName);
                                                    server.chalengedUsers.add(chalengedUser);
                                                    
                                                    this.sendMessage(chalengedUser, "6 " + userName);
                                                    this.sendMessage(userName, "4 " + "You have chalenged " + chalengedUser);
                                                }
                                            }
                                        } else {
                                            this.sendMessage(userName, "4 " + "User " + chalengedUser + " is already being chalenged");
                                        }
                                    }
                                } else {
                                    this.sendMessage(userName, "4 " + "You have already chalenged another player");
                                }                                
                            }
                                
                                
                            break; }
                        
                        case 5: { // Accept chalenge: 5 <<chalengerUser>>
                            // chalengedUser -> userName
                            String chalengerUser = st.nextToken();                            
                            this.sendMessage(chalengerUser, "7 " + userName);
                            synchronized(server.chalengers) {
                                server.chalengers.remove(chalengerUser);
                            }
                            synchronized(server.chalengedUsers) {
                                server.chalengedUsers.remove(userName);
                            }
                            synchronized(server.playingUsers) {
                                server.playingUsers.add(userName);
                                server.playingUsers.add(chalengerUser);
                            }
                            
                            break; }
                        
                        case 6: { // Decline chalenge: 6 <<chalengerUser>>
                            // chalengedUser -> userName
                            String chalengerUser = st.nextToken();
                            this.sendMessage(chalengerUser, "8 " + userName);
                            synchronized(server.chalengers) {
                                server.chalengers.remove(chalengerUser);
                            }
                            synchronized(server.chalengedUsers) {
                                server.chalengedUsers.remove(userName);
                            }
                            
                            break; }
                        case 7: { // End of game: 7 -> remove user from playingUsers list
                            synchronized(server.playingUsers) {
                                if(server.playingUsers.contains(userName)) server.playingUsers.remove(userName);
                            }
                                  break; }                        
                        }                   
                    }            
        } catch(EOFException ie ) {
            // This doesn't need an error message
        } catch( IOException ie ) {
            // This does; tell the world!
            //ie.printStackTrace();
        } finally {
            // The connection is closed for one reason or another,
            // so have the server dealing with it
            // We synchronize on this because another thread might be calling
            // another method that uses the same object
            synchronized(server.outputStreams) {                
                Socket s = (Socket)server.socketMap.get(userName);
                // Make sure it's closed
                try {
                    s.close();
                } catch( IOException ie ) {
                    System.out.println("Failde to remove !!!");
                } finally {
                    // Remove it from our hashtable/list
                    server.outputStreams.remove(userName);
                    server.socketMap.remove(userName);
                    server.gameRooms.remove(userName);
                    this.sendToAll("2 " + userName, currentGame);
                    this.sendToAll("4 " + "User " + userName + " has left gameRoom", currentGame);
                    System.out.println( "Removed connection to " + userName);
                }
            }
        }
    }
}