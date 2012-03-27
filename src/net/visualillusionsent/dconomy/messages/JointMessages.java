package net.visualillusionsent.dconomy.messages;

public enum JointMessages {
    
    /**
     * JointMessage301: AccountBalance
     */
    J301("\u00A7b<acc>\u00A7a balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * JointMessage302: NewBalance
     */
    J302("\u00A7b<acc>\u00A7a new balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * JointMessage303: Withdraw
     */
    J303("\u00A7aYou have withdrawn \u00A7e<a> <m> \u00A7a from \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage304: Deposit
     */
    J304("\u00A7aYou have deposited \u00A7e<a> <m> into \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage305: Created
     */
    J305("\u00A7aCreated JointAccount \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage306: OwnerAdded
     */
    J306("\u00A7aAdded \u00A76<p>\u00A7a as Owner to Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage307: OwnerRemoved
     */
    J307("\u00A7aRemoved \u00A76<p>\u00A7a as Owner from  Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage308: AccountDeleted
     */
    J308("\u00A7aDeleted Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage309: UserAdded
     */
    J309("\u00A7aAdded \u00A76<p>\u00A7a as User to Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage310: UserRemoved
     */
    J310("\u00A7aRemoved \u00A76<p>\u00A7a as User from Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * JointMessage311: MaxUserWithdrawCheck
     */
    J311("\u00A7aMax User Withdraw is \u00A7e<a> <m>\u00A7a per \u00A75<min> Minutes\u00A7a."),
    
    /**
     * JointMessage312: MaxUserWithdrawSet
     */
    J312("\u00A7aYou have set Max User Withdraw to \u00A7e<a> <m>\u00A7a.");
    
    private String mess;
    
    private JointMessages(String defmess){
        this.mess = defmess;
    }
    
    void setMess(String mess){
        this.mess = mess;
    }
    
    final String plainMess(){
        return mess;
    }
    
    public final String Mess(String username, String type, double amount){
        return LoadMessages.parseMessage(mess, username, type, amount);
    }
}
