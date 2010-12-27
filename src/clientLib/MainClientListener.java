package clientLib;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class MainClientListener extends Thread {
    // connection to the server
    protected Socket socket = null;
    // comunication streams
    protected DataOutputStream dout = null;
    protected DataInputStream din = null;
    // this client's userName
    private String userName, gameName;
    // reference to the object that created this thread
    private MainClient mainClient;
    private String realmIP = "";
    
    // constructor
    public MainClientListener(MainClient cm, Socket socket, String userName, String realmIP) {
        this.socket = socket;
        this.mainClient = cm;
        this.userName = userName;
        this.realmIP = realmIP;
    }
    
    public void run() {
        this.listen();
    }
    
    public void changeGame(String gameName) {
        if(this.gameName != gameName)  {
            this.gameName = gameName;
            try {
                this.refreshNewGame();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void refreshNewGame() throws IOException {
        // 2 <<gameName>>: Change game !!!!!
        dout.writeUTF("2 " + this.gameName);
        mainClient.removeAllUsers();
    }
    
    public void sendMessage(String message) throws IOException {
        System.out.println("^^^^ " + message);
        dout.writeUTF(message);
    } 
    
    public void pExit() {
        try {
            //JOptionPane.showMessageDialog(null, "DONEEEEEEEEE");
            this.sendMessage("7");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void execCommand(String gameName, String player1, String player2) {
        // Execute a command with an argument that contains a space
        String pathArgument = "CGames/" + gameName + "/" + gameName + ".jar";
        System.out.println("*** " + pathArgument + "  " + player1 + " " + player2 + " " + realmIP);
        String[] commands = new String[]{ "java", "-jar", pathArgument, player1, player2, realmIP };
        
        try {            
            Process p = Runtime.getRuntime().exec(commands);
            (new CommandProcess(p, this)).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        /*try {
            Process child = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        // Reflection
        /*Class cls = Class.forName("Foo");
        Method method = cls.getMethod("hello", null);
        method.invoke(cls.newInstance(), null);*/
    }
    
    private void listen() {
        try {
            this.din = new DataInputStream(socket.getInputStream());
            this.dout = new DataOutputStream(socket.getOutputStream());
            // let the sever knwo what out userName is and what game we're playin'
            dout.writeUTF("1 " + this.userName);
            
            // recieve messages
            while (! interrupted()) {
                // get the next message
                String message = din.readUTF();
                System.out.println("Message from server: " + message);
                
                // parse it
                StringTokenizer st = new StringTokenizer(message);
                switch (Integer.parseInt(st.nextToken())) {
                    case 1: { // List of available games
                        String[] games = new String[st.countTokens()];
                        int i = 0;
                        while(st.hasMoreTokens()) {
                            games[i] = st.nextToken();
                            i++;
                        }
                        mainClient.updateGames(games);
                        
                        break; }
                    case 2: { // User left gameRoom
                        mainClient.removeUser(st.nextToken());
                        break; }
                    case 3: { // User joined gameRoom
                        String auxUserName = st.nextToken();
                        System.out.println("@@@ " + auxUserName + " .. " + userName);
                        if(! auxUserName.equals(userName)) mainClient.addUser(auxUserName);
                        break; }
                    case 4: { // Get system message
                        String aux = "";
                        while(st.hasMoreTokens()) aux = aux + st.nextToken() + " ";
                        mainClient.updateChatPanel("SYSTEM MESSAGE", aux);
                        break; }
                    case 5: { // Get user message
                        String aux = "", auxUser = st.nextToken();
                        while(st.hasMoreTokens()) aux = aux + st.nextToken() + " ";
                        mainClient.updateChatPanel(auxUser, aux);
                        break; }
                    case 6: { // Get chalenged: 6 <<chalengerUser>>
                        String chalengerUser = st.nextToken();
                        mainClient.updateChatPanel("SYSTEM MESSAGE", "You have been chalenged by " + chalengerUser);
                        int answer = JOptionPane.showConfirmDialog(mainClient, "You have been chalenged by user " + chalengerUser + ". Accept ?", "Yagle confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (answer == JOptionPane.YES_OPTION) {
                            this.sendMessage("5 " + chalengerUser);
                            mainClient.updateChatPanel("SYSTEM MESSAGE", "You have accepted " + chalengerUser + "'s chalenge");
                            /*try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }*/                            
                            this.execCommand(gameName, userName, chalengerUser);
                            System.out.println("********* Started ******");
                        } else {
                            this.sendMessage("6 " + chalengerUser);
                            mainClient.updateChatPanel("SYSTEM MESSAGE", "You have declined " + chalengerUser + "'s chalenge");
                        }
                        
                        break; }
                    case 7: { // Chalenge accepted: 7 <<chelengedUser>>
                        String chalengedUser = st.nextToken();
                        //JOptionPane.showMessageDialog(null, "User " + chalengedUser + " has accepted your challenge");
                        mainClient.updateChatPanel("SYSTEM MESSAGE", "User " + chalengedUser + " has accepted your challenge");
                        /*try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }*/
                        System.out.println("********* Started ******");
                        this.execCommand(gameName, userName, chalengedUser);
                        System.out.println("********* Started ******");
                        break; }
                    case 8: { // Chalenge declined: 8 <<chelengedUser>>
                        String chalengedUser = st.nextToken();
                        //JOptionPane.showMessageDialog(null, "User " + chalengedUser + " has declined your challenge");
                        mainClient.updateChatPanel("SYSTEM MESSAGE", "User " + chalengedUser + " has declined your challenge");
                        
                        break; }             
                }
            }
        } catch( IOException ie ) { ie.printStackTrace(); }
    }
}