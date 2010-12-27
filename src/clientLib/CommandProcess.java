package clientLib;

import java.io.IOException;

public class CommandProcess extends Thread {
    private String[] command = null;
    private MainClientListener mcl;
    private Process p = null;
    
    /*public CommandProcess(String[] command, MainClientListener mcl) {
        this.command = command;
        this.mcl = mcl;
    }*/
    
    public CommandProcess(Process p, MainClientListener mcl) {
        this.mcl = mcl;
        this.p = p;
    }
    
    public void run() {
        //Process p;
       // try {
           // p = Runtime.getRuntime().exec(command);
            while (! procDone(p));    
       // } catch (IOException ex) {
        //    ex.printStackTrace();
        //} finally {
            mcl.pExit();
        //}
    }
    
    private boolean procDone(Process p) {
        try {
            int v = p.exitValue();
            return true;
        } catch(IllegalThreadStateException e) {
            return false;
        }
    }
}
