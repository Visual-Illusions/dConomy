package net.visualillusionsent.dconomy.messages;

/**
 * Handles help related messages
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since 2.0
 * @author darkdiplomat
 */
public enum HelpMessages {
    
    /**
     *  HelpMessage401: MoneyHelpOpen
     */
    H401("\u00A72|--------------\u00A76dConomy \u00A7l/money\u00A72 Help\u00A72--------------|"),
    
    /**
     * HelpMessage402: BankHelpOpen
     */
    H402("\u00A72|--------------\u00A76dConomy \u00A7l/bank\u00A72 Help\u00A72--------------|"),
    
    /**
     * HelpMessage403: JointHelpOpen
     */
    H403("\u00A72|--------------\u00A76dConomy \u00A7l/joint\u00A72 Help\u00A72--------------|"),
    
    /**  
     *  HelpMessage404: RequiredOptionalAlias
     */
    H404("\u00A72|------\u00A7c<REQUIRED>\u00A72----\u00A7b[Optional]\u00A72------|"),
    
    /**
     * HelpMessage405: PlayerAmountAccount
     */
    H405("\u00A72|----\u00A76 p = Player \u00A7ea = Amount \u00A7bacc = Account \u00A72----|"),
    
    /**
     * HelpMessage406: UseMoneyHelp
     */
    H406("   \u00A72Use \u00A76/money help \u00A72for help with Money commands"),
    
    /**
     * HelpMessage407: UseBankHelp
     */
    H407("   \u00A72Use \u00A76/bank help \u00A72for help with Bank commands"),
    
    /**
     * HelpMessage408: UseJointHelp
     */
    H408("   \u00A72Use \u00A76/joint help \u00A72for help with Joint Accounts"),
    
    /**  
     *  HelpMessage409: MoneyBase
     */
    H409("   \u00A76\u00A7l/money\u00A72: Base Command + Display account balance"),
    
    /**
     * HelpMessage410: BankBase
     */
    H410("   \u00A76\u00A7l/bank\u00A72: Base Command + Display bank balance"),
    
    /**
     * HelpMessage411: JointBase
     */
    H411("   \u00A76\u00A7l/joint\u00A72: Base Command + Display JointAccount balance"),
    
    /**
     * HelpMessage412: RankCommand
     */
    H412("   \u00A76\u00A7lrank \u00A7b[p/acc]\u00A72: Display Rank Position"),
    
    /**
     * HelpMessage413: RankTopCommand
     */
    H413("   \u00A76\u00A7ltop \u00A7b[a]\u00A72: Display Richest Accounts"),
    
    /**
     * HelpMessage414: MoneyPayCommand
     */
    H414("   \u00A76\u00A7lpay \u00A7c<p> <a>\u00A72: Pay account Amount"),
    
    /**
     * HelpMessage415: MoneyAuto
     */
    H415("   \u00A76\u00A7lauto \u00A72: checks PayForwarding account settings"),
    
    /**
     * HelpMessage416: MoneySetAuto
     */
    H416("   \u00A76\u00A7lsetauto \u00A7c<acc|OFF>\u00A72: Changes or sets PayForwarding account"),
    
    /**
     * HelpMessage417: BankWithdraw
     */
    H417("   \u00A76\u00A7lwithdraw \u00A7c<a>\u00A72: withdraw from account"),
    
    /**
     * HelpMessage418: BankDeposit
     */
    H418("   \u00A76\u00A7ldeposit \u00A7c<a>\u00A72: deposit into account"),
    
    /**
     * HelpMessage419: JointWithdraw
     */
    H419("   \u00A76\u00A7lwithdraw \u00A7c<acc> <a>\u00A72: withdraw from Account"),
   
    /**
     * HelpMessage420: JointDeposit
     */
    H420("   \u00A76\u00A7ldeposit \u00A7c<acc> <a>\u00A72: deposit into Account"),
    
    /**
     * HelpMessage421: JointPay
     */
    H421("   \u00A76\u00A7lpay \u00A7c<acc> <a>\u00A72: pay Account"),
    
    /**
     * HelpMessage422: JointCreate
     */
    H422("   \u00A76\u00A7lcreate \u00A7c<acc>\u00A72: create Account"),
    
    /**
     * HelpMessage423: JointDelete
     */
    H423("   \u00A76\u00A7ldelete \u00A7c<acc>\u00A72: deletes Account"),
    
    /**
     * HelpMessage424: JointAddOwner
     */
    H424("   \u00A76\u00A7laddowner \u00A7c<acc> <p>\u00A72: add owner to Account"),
    
    /**
     * HelpMessage425: JointRemoveOwner
     */
    H425("   \u00A76\u00A7lremoveowner \u00A7c<acc> <p>\u00A72: remove owner from Account"),
    
    /**
     * HelpMessage426: JointAddUser
     */
    H426("   \u00A76\u00A7ladduser \u00A7c<acc> <p>\u00A72: add user to Account"),
    
    /**
     * HelpMessage427: JointRemoveUser
     */
    H427("   \u00A76\u00A7lremoveuser \u00A7c<acc> <p>\u00A72: remove user from Account"),
    
    /**
     * HelpMessage428: JointSetUserMax
     */
    H428("   \u00A76\u00A7lsetusermax \u00A7c<a>\u00A72: set user's Max Withdraw Amount per set delay"),
    
    /**
     * HelpMessage429: JointUserMaxCheck
     */
    H429("   \u00A76\u00A7lusermax \u00A7c<acc> <a>\u00A72: checks UserMaxWithdraw per set delay"),
    
    /**
     * HelpMessage430: JointSetUserWithdrawDelay
     */
    H430("   \u00A76\u00A7lsetdelay \u00A7c<acc> <a>\u00A72: sets User Max Withdraw delay"),
    
    /**
     * HelpMessage431: AccountSet
     */
    H431("   \u00A76\u00A7lset \u00A7c<acc/p> <a>\u00A72: Set account balance to amount"),
    
    /**
     * HelpMessage408: AccountReset
     */
    H432("   \u00A76\u00A7lreset \u00A7c<acc/p>\u00A72: Reset account balance to default"),
    
    /**
     * HelpMessage409: AccountAdd
     */
    H433("   \u00A76\u00A7ladd \u00A7c<acc/p> <a>\u00A72: Add amount to account"),
    
    /**
     * HelpMessage410: AccountRemove
     */
    H434("   \u00A76\u00A7lremove \u00A7c<acc/p> <a>\u00A72: Remove amount from account");
    
    private String mess;
                    
    private HelpMessages(String defmess){
        this.mess = defmess;
    }
                    
    void setMess(String mess){
        this.mess = mess;
    }
                    
    final String plainMess(){
        return mess;
    }
    
    /**
     * Gets the help message
     * 
     * @return message
     */
    public final String Mess(){
        return mess;
    }
}
