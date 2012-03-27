package net.visualillusionsent.dconomy.messages;

public enum ErrorMessages {
    
    /**
     * Error101: Player-NotEnoughMoney
     */
    E101("\u00A7cYou do not have enough <m> to complete transaction!"),
    
    /**
     * Error102: Bank-NotEnoughMoney
     */
    E102("\u00A7cYour bank does not have enough <m> to complete transaction!"),
    
    /**
     * Error103: NumberFormatError
     */
    E103("\u00A7cYou did not enter a proper number!"),
    
    /**
     * Error104: Command-NoPermission
     */
    E104("\u00A7cYou do not have permission to use that command!"),
    
    /**
     * Error105: Joint-NotEnoughMoney
     */
    E105("\u00A7c<acc> does not have the funds for this transaction!"),
    
    /**
     * Error106: AmountNotSpecified
     */
    E106("\u00A7cYou didn't specify an amount!"),
    
    /**
     * Error107: Joint-NoAccess
     */
    E107("\u00A7cYou do not have access to <acc>!"),
    
    /**
     * Error108: PlayerNotFound
     */
    E108("\u00A7cPlayer not found!"),
    
    /**
     * Error109: AccountNotFound
     */
    E109("\u00A7cCould not find account for <p>!"),
    
    /**
     * Error110: JointNotFound
     */
    E110("\u00A7c<acc> does not exist!"),
    
    /**
     * Error111: JointNameTaken
     */
    E111("\u00A7c<acc> is already in use!"),
    
    /**
     * Error112: JointOwnerNotSpecified
     */
    E112("\u00A7cYou didn't specify an owner!"),
    
    /**
     * Error113: InvalidCommand
     */
    E113("\u00A7cInvaild dConomy Command!"),
    
    /**
     * Error114: Joint-PlayerNotSpecified
     */
    E114("\u00A7cYou didn't specify a player!"),
    
    /**
     * Error115: JointNameTooLong
     */
    E115("\u00A7cAccount names cannot be longer than 32 Characters!"),
    
    /**
     * Error116: Joint-User-CannotWithdrawAgainYet
     */
    E116("\u00A7cYou cannot make another withdraw for <xmin> Minutes!"),
    
    /**
     * Error117: Joint-User-WithdrawTooMuch
     */
    E117("\u00A7cYou cannot withdraw that amount at one time!"),
    
    /**
     * Error118: Player-CannotPaySelf
     */
    E118("\u00A7cYou cannot pay yourself!"),
    
    /**
     * Error119: Player-AlreadyJointUser
     */
    E119("\u00A7c<p> is already a User!"),
    
    /**
     * Error120: PlayerNotSpecified
     */
    E120("\u00A7cPlayer Not Specified!"),
    
    /**
     * Error121: Joint-UserNotSpecified
     */
    E121("\u00A7cJoint Account User not specified!"),
    
    /**
     * Error122: Joint-PlayerAlreadyOwner
     */
    E122("\u00A7c<p> is already a Owner!"),
    
    /**
     * Error123: Joint-OwnerNotSpecified
     */
    E123("\u00A7cJoint Account Owner not specified!"),
    
    /**
     * Error124: Joint-PlayerAlreadyNotUser
     */
    E124("\u00A7c<p> wasn't a User!"),
    
    /**
     * Error125: Joint-PlayerAlreadyNotOwner
     */
    E125("\u00A7c<p> wasn't a Owner!"),
    
    /**
     * Error126: NegativeNumber
     */
    E126("\u00A7cAmount cannot be a negative!"),
    
    /**
     * Error127: BalanceNegative
     */
    E127("\u00A7cBalance cannot become negative!");
    
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
