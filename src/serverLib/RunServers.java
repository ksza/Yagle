package serverLib;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class RunServers extends javax.swing.JFrame {
    
    private Vector<Process> processArray = new Vector<Process>();
    
    public RunServers() {
        initComponents();
        this.setTitle("Yagle: start servers");
        messageWindow.setEditable(false);
        stopServersButton.setEnabled(false);
    }   
   
    private void execCommand(String serverName) {
        // Execute a command with an argument that contains a space       
        messageWindow.append(serverName + " -----> STARTED\n");
        String[] commands = new String[]{ "java", "-jar", serverName };
        try {
            processArray.add(Runtime.getRuntime().exec(commands));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        startServersButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageWindow = new javax.swing.JTextArea();
        stopServersButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        startServersButton.setText("StartServers");
        startServersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startServersButtonMouseClicked(evt);
            }
        });

        messageWindow.setColumns(20);
        messageWindow.setRows(5);
        jScrollPane1.setViewportView(messageWindow);

        stopServersButton.setText("StopServers");
        stopServersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stopServersButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startServersButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(stopServersButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startServersButton)
                    .addComponent(stopServersButton))
                .addContainerGap())
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stopServersButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopServersButtonMouseClicked
        for(int i = 0; i < processArray.size(); i++) {
            ((Process)processArray.get(i)).destroy();            
        }
        messageWindow.removeAll();
        startServersButton.setEnabled(true);
        stopServersButton.setEnabled(false);
    }//GEN-LAST:event_stopServersButtonMouseClicked

    private void startServersButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startServersButtonMouseClicked
        execCommand("YagleRegisterServer.jar");
        execCommand("YaglePlayServer.jar");
        
        File dir = new File("SGames");
        if(dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                execCommand("SGames/" + children[i] + "/" + children[i] + "Server.jar");
            }
        }
        
        stopServersButton.setEnabled(true);
        startServersButton.setEnabled(false);
    }//GEN-LAST:event_startServersButtonMouseClicked
        
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RunServers().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea messageWindow;
    private javax.swing.JButton startServersButton;
    private javax.swing.JButton stopServersButton;
    // End of variables declaration//GEN-END:variables
    
}
