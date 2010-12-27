package clientLib;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class MainClient extends javax.swing.JFrame {
    
    public MainClient(String userName) {
        this.userName = userName;
        initComponents();
        chatPanel.setEditable(false);
        systemPanel.setEditable(false);
        messageField.setText("");
        
        try {
            // create a new connection
            this.socket = new Socket(hostAddress, port);
            System.out.println( "connected to " + socket );
            
            this.dout = new DataOutputStream(socket.getOutputStream());
            // create a new thread to handle the data flow
            fir1 = new MainClientListener(this, socket, this.userName, this.hostAddress);
            fir1.start();
        } catch(IOException e) {}
        
        this.setTitle("Yagle player: " + userName);
    }
    
    protected void updateGames(String[] games) {
        gamesList.removeAll();
        for(int i = 0; i < games.length; i++)
            gamesList.add(games[i]);
    }
    
    protected void addUser(String userName) {
        usersList.add(userName);
    }
    
    protected void removeUser(String userName) {
        usersList.remove(userName);
    }
    
    protected void removeAllUsers() {
        usersList.removeAll();
    }
    
    private void sendText() {
        try {
            fir1.sendMessage("3 " + messageField.getText());
            messageField.setText("");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void updateChatPanel(String userName, String message) {  
        message = message.trim();
        if(userName.equals("SYSTEM MESSAGE")) { 
            systemPanel.setForeground(Color.RED);
            systemPanel.append(message + "\n");        
        } else {
            chatPanel.append(userName + ": " + message + "\n");            
        }
    }       
       
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        selectGameButton = new javax.swing.JButton();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        challengeButton = new javax.swing.JButton();
        viewProfileButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        chatPanel = new javax.swing.JTextArea();
        gamesList = new java.awt.List();
        usersList = new java.awt.List();
        jScrollPane1 = new javax.swing.JScrollPane();
        systemPanel = new javax.swing.JTextArea();
        systemMessagesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        selectGameButton.setText("Select Game");
        selectGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectGameButtonMouseClicked(evt);
            }
        });

        messageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageFieldKeyPressed(evt);
            }
        });

        sendButton.setText("Send");
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendButtonMouseClicked(evt);
            }
        });

        challengeButton.setText("Challenge");
        challengeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                challengeButtonMouseClicked(evt);
            }
        });

        viewProfileButton.setText("View Profile");

        quitButton.setText("Quit");

        chatPanel.setColumns(20);
        chatPanel.setFont(new java.awt.Font("Monospaced", 0, 11));
        chatPanel.setRows(5);
        jScrollPane4.setViewportView(chatPanel);

        gamesList.setFont(new java.awt.Font("Dialog", 0, 11));

        usersList.setFont(new java.awt.Font("Dialog", 0, 11));

        systemPanel.setColumns(20);
        systemPanel.setFont(new java.awt.Font("Monospaced", 0, 11));
        systemPanel.setForeground(new java.awt.Color(255, 51, 51));
        systemPanel.setRows(5);
        jScrollPane1.setViewportView(systemPanel);

        systemMessagesLabel.setForeground(new java.awt.Color(255, 51, 51));
        systemMessagesLabel.setText("System Messages: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usersList, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(gamesList, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(selectGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(messageField, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(challengeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewProfileButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(quitButton))
                    .addComponent(systemMessagesLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(gamesList, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectGameButton))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sendButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(systemMessagesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(challengeButton)
                            .addComponent(viewProfileButton)
                            .addComponent(quitButton)))
                    .addComponent(usersList, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void challengeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_challengeButtonMouseClicked
        if(usersList.getSelectedIndex() < 0) JOptionPane.showMessageDialog(null, "Select a player first !");
        else {
            String chalengedPlayer = usersList.getSelectedItem();  
            try {
                fir1.sendMessage("4 " + chalengedPlayer);
            } catch(IOException e) { e.printStackTrace(); }
        }
    }//GEN-LAST:event_challengeButtonMouseClicked

    private void messageFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messageFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_ENTER) {
            this.sendText();
        }
    }//GEN-LAST:event_messageFieldKeyPressed

    private void sendButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendButtonMouseClicked
        this.sendText();
    }//GEN-LAST:event_sendButtonMouseClicked

    private void selectGameButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectGameButtonMouseClicked
        if(gamesList.getSelectedIndex() < 0) JOptionPane.showMessageDialog(null, "Select a game first !");
        else {
            String selectedGame = gamesList.getSelectedItem();
            File fis = new File("CGames/" + selectedGame + "/" + selectedGame + ".jar");
            if(! fis.exists()) {
                JOptionPane.showMessageDialog(this, "This game must be updated !");
            } else {
                int answer = JOptionPane.showConfirmDialog(this, "Do you realy want to switch to the selected game ?", "Yagle confiramtion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer == JOptionPane.YES_OPTION) {
                    fir1.changeGame(gamesList.getSelectedItem());
                }
            }
        }
    }//GEN-LAST:event_selectGameButtonMouseClicked
    
     private String getServerIP() {
        String realmIP = null;
        try {
            File file = new File("servers.rem");            
            if(! file.exists()) System.out.println("server.rem file is missing !");
            else {
                FileReader fileReader = new FileReader("servers.rem");
                BufferedReader buffReader = new BufferedReader(fileReader);                
                realmIP = buffReader.readLine();                
                buffReader.close();
            }
        } catch (IOException e) {
        } finally {            
            return realmIP;
        }    
    }
    
     /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainClient("Guest1").setVisible(true);
            }
        });
     }*/
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton challengeButton;
    private javax.swing.JTextArea chatPanel;
    private java.awt.List gamesList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField messageField;
    private javax.swing.JButton quitButton;
    private javax.swing.JButton selectGameButton;
    private javax.swing.JButton sendButton;
    private javax.swing.JLabel systemMessagesLabel;
    private javax.swing.JTextArea systemPanel;
    private java.awt.List usersList;
    private javax.swing.JButton viewProfileButton;
    // End of variables declaration//GEN-END:variables
    
    private String userName;
    private String hostAddress = getServerIP();
    // the port address
    private int port = 4445;
    private Socket socket = null;
    private DataOutputStream dout;
    private MainClientListener fir1;
}
