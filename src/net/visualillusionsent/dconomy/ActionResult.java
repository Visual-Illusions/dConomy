package net.visualillusionsent.dconomy;

public class ActionResult {
    private String[] mess;
    private String[] othermess = null;
    private String otherreceiver = null;
    
    public ActionResult(){
        mess = new String[]{""};
    }
    
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
