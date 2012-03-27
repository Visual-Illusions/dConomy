package net.visualillusionsent.dconomy.messages;

public enum PlayerMessages {
   
    /**
     * PlayerMessage201: AccountBalance
     */
    P201("\u00A7aYour \u00A7bAccount Balance\u00A7a is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * PlayerMessage202: BankBalance
     */
    P202("\u00A7aYour \u00A7bBank Balance\u00A7a is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * PlayerMessage203: BankWithdraw
     */
    P203("\u00A7aYour have withdrawn \u00A7e<a> <m>\u00A7a from your \u00A7bBank Account\u00A7a."),
    
    /**
     * PlayerMessage204: BankDeposit
     */
    P204("\u00A7aYour have deposited \u00A7e<a> <m>\u00A7a into your \u00A7bBank Account\u00A7a."),
    
    /**
     * PlayerMessage205: BankNewBalance
     */
    P205("\u00A7aYour new bank balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * PlayerMessage206: AccountNewBalance
     */
    P206("\u00A7aYour new balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * PlayerMessage207: SentPaymentTo
     */
    P207("\u00A7aYou have sent \u00A7e<a> <m> to \u00A76<p>\u00A7a."),
    
    /**
     * PlayerMessage208: ReceivedPaymentFrom
     */
    P208("\u00A7aYou have received \u00A7e<a> <m>\u00A7a from \u00A76<p>\u00A7a."),
    
    /**
     * PlayerMessage209: RankSelf
     */
    P209("\u00A7aYou are ranked \u00A7e<rank>\u00A7a."),
    
    /**
     * PlayerMessage210: RankOther
     */
    P210("\u00A76<p>\u00A7a is ranked \u00A7e<rank>\u00A7a."),
    
    /**
     * PlayerMessage211: PayForwardJoint
     */
    P211("\u00A7aYour pay has been forwarded to JointAccount: \u00A7b<acc>\u00A7a."),
    
    /**
     * PlayerMessage212: PaymentSentFromJoint
     */
    P212("\u00A7aPayment has been sent from Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * PlayerMessage213: PayForwardBank
     */
    P213("\u00A7aYour pay has been forwarded to your \u00A7bBank Account\u00A7a."),
    
    /**
     * PlayerMessage214: PaymentSentFromBank
     */
    P214("\u00A7aPayment has been sent from your \u00A7bBank Account\u00A7a."),
    
    /**
     * PlayerMessage215: PayForwardSetJoint
     */
    P215("\u00A7aYou have set up Pay Forwarding to Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * PlayerMessage216: PayForwardSetBank
     */
    P216("\u00A7aYou have set up Pay Forwarding to your \u00A7bBank Account\u00A7a."),
    
    /**
     * PlayerMessage217: PaymentSentToJoint
     */
    P217("\u00A7aYou have paid <amount> to Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * PlayerMessage218: PayForwardTurnedOff
     */
    P218("\u00A7aYou have turned Pay Forwarding \u00A7cOFF\u00A7a."),
    
    /**
     * PlayerMessage219: PayForwardingIsSetJoint
     */
    P219("\u00A7aYou have Pay Forwarding set to Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * PlayerMessage220: PayForwardingIsSetBank
     */
    P220("\u00A7aYou have Pay Forwarding set to your \u00A7b Bank Account\u00A7a."),
    
    /**
     * PlayerMessage221: PayForwardingNotActive
     */
    P221("\u00A7aPay Forwarding is not active."),
    
    /**
     * PlayerMessage222: AccountCheck
     */
    P222("\u00A76<p>'s\u00A7a Account Balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * PlayerMessage223: BankCheck
     */
    P223("\u00A76<p>'s\u00A7a Bank Balance is \u00A7e<a> <m>\u00A7a.");
    
    private String mess;
    
    private PlayerMessages(String defmess){
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
