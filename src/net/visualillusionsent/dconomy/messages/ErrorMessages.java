package net.visualillusionsent.dconomy.messages;

public enum ErrorMessages {
    
    
    /**
     * Error101: NoPermission
     */
    E101("\u00A7cYou do not have permission to use that command!"),
    
    /**
     * Error102: NumberFormatError
     */
    E102("\u00A7cYou did not enter a proper number!"),
    
    /**
     * Error103: NotEnoughArguments
     */
    E103("\u00A7cYou didn't give enough arguments!"),
    
    /**
     * Error104: AccountNotFound
     */
    E104("\u00A7cCould not find account for <p>!"),
    
    /**
     * Error105: JointAccountNotFound
     */
    E105("\u00A7c<acc> does not exist!"),
    
    /**
     * Error106: JointAccountNameTaken
     */
    E106("\u00A7c<acc> is already in use!"),
    
    /**
     * Error107: JointAccountNameInvalid
     */
    E107("\u00A7cYou have entered illegal characters for an account name!"),
    
    /**
     * Error108: JointAccountNoPermission
     */
    E108("\u00A7cYou do not have access to <acc>!"),
    
    /**
     * Error109: JointAccountUserCannotWithdraw
     */
    E109("\u00A7cYou cannot make another withdraw for <xmin> Minutes!"),
    
    /**
     * Error110: JointAccountUserWithdrawTooMuch
     */
    E110("\u00A7cYou cannot withdraw that amount at one time!"),
    
    /**
     * Error111: JointAccountAlreadyUser
     */
    E111("\u00A7c<p> is already a User!"),
    
    /**
     * Error122: JointAccountAlreadyOwner
     */
    E112("\u00A7c<p> is already a Owner!"),
    
    /**
     * Error113: JointAccountAlreadyNotUser
     */
    E113("\u00A7c<p> wasn't a User!"),
    
    /**
     * Error114: JointAccountAlreadyNotOwner
     */
    E114("\u00A7c<p> wasn't a Owner!"),
    
    /**
     * Error115: AccountNotEnoughMoney
     */
    E115("\u00A7cYou do not have enough <m> to complete transaction!"),
    
    /**
     * Error116: BankNotEnoughMoney
     */
    E116("\u00A7cYour bank does not have enough <m> to complete transaction!"),
    
    /**
     * Error117: JointNotEnoughMoney
     */
    E117("\u00A7c<acc> does not have the funds for this transaction!"),
    
    /**
     * Error118: PlayerCannotPaySelf
     */
    E118("\u00A7cYou cannot pay yourself!");
    
    private String mess;
    
    private ErrorMessages(String defmess){
        this.mess = defmess;
    }
    
    void setMess(String mess){
        this.mess = mess;
    }
    
    final String plainMess(){
        return mess;
    }
    
    public final String Mess(String arg){
        return LoadMessages.parseError(mess, arg);
    }
}
