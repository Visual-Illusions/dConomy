package net.visualillusionsent.dconomy.messages;

/**
 * Handles account related messages
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since 2.0
 * @author darkdiplomat
 */
public enum AccountMessages {
    
    /**
     * AccountMessage201: AccountBalance
     */
    A201("\u00A7aYour \u00A7b\u00A7l<acc>\u00A7a balance\u00A7a is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AccountMessage202: AccountNewBalance
     */
    A202("\u00A7aYour new \u00A7b\u00A7l<acc>\u00A7a balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AccountMessage203: CheckAnotherAccount
     */
    A203("\u00A76<p>'s \u00A7b\u00A7l<acc>\u00A7a balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AccountMessage204: BankDeposit
     */
    A204("\u00A7aYou have deposited \u00A7e<a> <m>\u00A7a into your \u00A7b\u00A7lBank\u00A7a."),
    
    /**
     * AccountMessage205: BankWithdraw
     */
    A205("\u00A7aYou have withdrawn \u00A7e<a> <m>\u00A7a from your \u00A7b\u00A7lBank\u00A7a."),
    
    /**
     * AccountMessage206: JointWithdraw
     */
    A206("\u00A7aYou have withdrawn \u00A7e<a> <m> \u00A7a from JointAccount: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage207: JointDeposit
     */
    A207("\u00A7aYou have deposited \u00A7e<a> <m> into JointAccount: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage208: PayPlayer
     */
    A208("\u00A7aYou have sent \u00A7e<a> <m>\u00A7a to \u00A76\u00A7l<p>"),
    
    /**
     * AccountMessage209: PayJoint
     */
    A209("\u00A7aYou have sent \u00A7e<a> <m>\u00A7a to JointAccount: \u00A7b\u00A7l <acc>\u00A7a."),
    
    /**
     * AccountMessage210: PaymentSentFromBank
     */
    A210("\u00A7aPayment was sent from your \u00A7b\u00A7lBank\u00A7a."),
    
    /**
     * AccountMessage211: PaymentSentFromJoint
     */
    A211("\u00A7aPayment was sent from JointAccount: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage212: ReceivedPaymentFrom
     */
    A212("\u00A7aYou have received \u00A7e<a> <m>\u00A7a from \u00A76\u00A7l<p>\u00A7a."),
    
    /**
     * AccountMessage213: PaymentSentToBank
     */
    A213("\u00A7aPayment was sent to your \u00A7b\u00A7lBank\u00A7a."),
    
    /**
     * AccountMessage214: PaymentSentToJointAccount
     */
    A214("\u00A7aPayment was sent to Joint Account: \u00A7b\u00A7l<acc>."),
    
    /**
     * AccountMessage215: PayForwardingSetToBank
     */
    A215("\u00A7aYou have set \u00A7dPay Forwarding\u00A7a to your \u00A7b\u00A7lBank\u00A7a."),
    
    /**
     * AccountMessage216: PayForwardingSetToJoint
     */
    A216("\u00A7aYou have set \u00A7dPay Forwarding\u00A7a to JointAccount: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage217: PayForwardingTurnedOff
     */
    A217("\u00A7aYou have turned \u00A7cOFF \u00A7dPay Forwarding\u00A7a."),
    
    /**
     * AccountMessage218: PayForwardingNotSet
     */
    A218("\u00A7aYou do not have \u00A7dPayForwarding \u00A7aset."),
    
    /**
     * AccountMessage219: PayForwardingIsBank
     */
    A219("\u00A7aYou have \u00A7dPay Forwarding\u00A7a set to your \u00A7b\u00A7lBank."),
    
    /**
     * AccountMessage220: PayForwardingIsJoint
     */
    A220("\u00A7aYou have \u00A7dPay Forwarding\u00A7a set to JointAccount: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage221: RankSelf
     */
    A221("\u00A7aYou are ranked \u00A7e<rank>\u00A7a."),
    
    /**
     * AccountMessage222: RankOther
     */
    A222("\u00A76\u00A7l<p> \u00A7ais ranked \u00A7e<rank>\u00A7a."),
    
    /**
     * AccountMessage 223: RankTopOpenner
     */
    A223("\u00A72-----Top \u00A7e<rank> \u00A72Richest \u00A7b\u00A7l<acc>s\u00A72-----"),
    
    /**
     * AccountMessage224: RankTopSort
     */
    A224("\u00A72   <rank>. \u00A7e<acc> \u00A72[\u00A7e<a> <m>\u00A72]"),
    
    /**
     * AccountMessage225: JointAccountCreated
     */
    A225("\u00A7aCreated JointAccount \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage226: JointAccountDeleted
     */
    A226("\u00A7aDeleted Joint Account: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage227: JointAccountOwnerAdded
     */
    A227("\u00A7aAdded \u00A76\u00A7l<p>\u00A7a as \u00A7dOwner\u00A7a to Joint Account: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage228: JointAccountOwnerRemoved
     */
    A228("\u00A7aRemoved \u00A76\u00A7l<p>\u00A7a as \u00A7d\u00A7lOwner\u00A7a from Joint Account: \u00A7b<acc>\u00A7a."),
    
    /**
     * AccountMessage229: JointAccountUserAdded
     */
    A229("\u00A7aAdded \u00A76\u00A7l<p>\u00A7a as \u00A7dUser\u00A7a to Joint Account: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage230: JointAccountUserRemoved
     */
    A230("\u00A7aRemoved \u00A76\u00A7l<p>\u00A7a as \u00A7dUser\u00A7a from Joint Account: \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AccountMessage231: JointAccountMaxUserWithdrawCheck
     */
    A231("\u00A7aMax User Withdraw is \u00A7e<a> <m>\u00A7a per \u00A75<min> Minutes\u00A7a."),
    
    /**
     * AccountMessage232: JointAccountMaxUserWithdrawSet
     */
    A232("\u00A7aYou have set Max User Withdraw to \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AccountMessage233: JointAccountMaxUserWithdrawDelaySet
     */
    A233("\u00A7aYou have set Max User Withdraw delay to \u00A7e<min> Minutes\u00A7a."),
    
    /**
     * AccountMessage234: JointAccountBalance
     */
    A234("\u00A7aJoint Account: \u00A7b\u00A7l<acc>'s\u00A7a balance is \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AccountMessage235: JointAccountNewBalance
     */
    A235("\u00A7aJoint Account: \u00A7b\u00A7l<acc>'s\u00A7a new balance is \u00A7e<a> <m>\u00A7a.");
    
    private String mess;
    
    private AccountMessages(String defmess){
        this.mess = defmess;
    }
    
    void setMess(String mess){
        this.mess = mess;
    }
    
    final String plainMess(){
        return mess;
    }
    
    /**
     * Handles the parsing of the message
     * 
     * @param username
     * @param type
     * @param amount
     * @param rank
     * @return message
     */
    public final String Mess(String username, String type, double amount, int rank){
        return LoadMessages.parseMessage(mess, username, type, amount, rank);
    }
}
