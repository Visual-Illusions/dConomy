package net.visualillusionsent.dconomy;

/**
 * ActionResult.java - dConomy Class for handling the sending of player messages
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class ActionResult {
    private String[] mess;
    private String[] othermess = null;
    private String otherreceiver = null;
    
    /**
     * Class Constructor
     */
    public ActionResult(){
        mess = new String[]{""};
    }
    
    /**
     * Sets the result messages
     * 
     * @param mess String array of messages to be sent
     */
    public void setMess(String[] mess){
        this.mess = mess;
    }
    
    public void setOtherMess(String receiver, String[] mess){
        this.otherreceiver = receiver;
        this.othermess = mess;
    }
    
    public String[] getMess(){
        return mess;
    }
    
    public String getOtherReceiver(){
        return otherreceiver;
    }
    
    public String[] getOtherMess(){
        return othermess;
    }
}
