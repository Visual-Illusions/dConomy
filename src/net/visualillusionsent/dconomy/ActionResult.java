package net.visualillusionsent.dconomy;

/**
 * ActionResult.java - dConomy Class for handling the sending of player messages
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class ActionResult {
    private String[] mess;
    private String[] othermess = null;
    private String otherreceiver = null;
    
    /**
     * Class constructor.
     * 
     * @since   2.0
     */
    public ActionResult(){
        mess = new String[]{""};
    }
    
    /**
     * Sets the result messages.
     * 
     * @param mess  String array of messages to be sent.
     * @since   2.0
     */
    public void setMess(String[] mess){
        this.mess = mess;
    }
    
    /**
     * Sets the result other messages.
     * 
     * @param receiver  The receivers name.
     * @param mess      String array of the messages to be sent.
     * @since   2.0
     */
    public void setOtherMess(String receiver, String[] mess){
        this.otherreceiver = receiver;
        this.othermess = mess;
    }
    
    /**
     * Gets the messages to be sent.
     * 
     * @return mess     String array of messages to be sent.
     * @since   2.0
     */
    public String[] getMess(){
        return mess;
    }
    
    /**
     * Gets the receiver for the other messages.
     * 
     * @return otherreceiver    The name of the other receiver.
     * @since   2.0
     */
    public String getOtherReceiver(){
        return otherreceiver;
    }
    
    /**
     * Gets the other messages to be sent
     * 
     * @return othermess    String array of the other messages.
     * @since   2.0
     */
    public String[] getOtherMess(){
        return othermess;
    }
}
