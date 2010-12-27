package serverLib;

import java.io.*;
import java.net.*;
import java.util.*;

public class RegisterServerThread extends Thread {
    // The RegisterServer that spawned us
    private RegisterServer server;
    // The Socket connected to our client
    private Socket socket;
    // this client's userName
    String nickName;
    
    // constructor
    public RegisterServerThread(RegisterServer server, Socket socket) {
        // Save the parameters
        this.server = server;
        this.socket = socket;
        // Start up the thread
        start();
    }
    
    // This runs in a separate thread when start() is called in the constructor.
    public void run() {
        try {
            // Create a DataInputStream for communication
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            
            String message = null;
            // read the next message
            message = din.readUTF();
            
            // parse the message: <<userName>> <<password>> <<emailAddress>>
            StringTokenizer st = new StringTokenizer(message);
            String aux = st.nextToken();
            
            if(aux.equals("1")) {
                // Register
                String userName = st.nextToken(), password = st.nextToken(), emailAddress = st.nextToken();
                
                try {
                    File file = new File("Users/" + userName + ".usf");
                    
                    // Create file if it does not exist
                    boolean success = file.createNewFile();
                    if (success) {
                        
                        FileWriter fileWriter = new FileWriter("Users/" + userName + ".usf");
                        BufferedWriter buffWriter = new BufferedWriter(fileWriter);
                        
                        buffWriter.write(password + "\n");
                        buffWriter.write(emailAddress + "\n");
                        
                        buffWriter.close();
                        
                        dout.writeUTF("Account successfully created !");
                    } else {
                        System.out.println("File already exists !");
                        dout.writeUTF("Error: Account already exists !");
                    }
                } catch (IOException e) {
                }
            } else {
                // Login
                String userName = st.nextToken(), password = st.nextToken();
                
                try {
                    File file = new File("Users/" + userName + ".usf");
                    if(! file.exists()) dout.writeUTF("Incorrect user name or password !");
                    else {
                        FileReader fileReader = new FileReader("Users/" + userName + ".usf");
                        BufferedReader buffReader = new BufferedReader(fileReader);
                        
                        String pass = buffReader.readLine();                        
                        if(pass.equals(password)) {
                            dout.writeUTF("Access granted !");
                        } else {
                            dout.writeUTF("Incorrect user name or password !");
                            System.out.println("Incorrect pass");
                        }
                        
                        buffReader.close();
                    }
                } catch (IOException e) {
                }
            }
        } catch( EOFException ie ) {
            // This doesn't need an error message
        } catch( IOException ie ) {
            // This does; tell the world!
            //ie.printStackTrace();
        } finally {
        }
    }
}