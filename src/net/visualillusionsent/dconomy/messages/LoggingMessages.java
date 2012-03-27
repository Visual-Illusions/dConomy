package net.visualillusionsent.dconomy.messages;

public enum LoggingMessages{
    
    /**
     * LoggingMessage601: PlayerPayPlayer
     */
    L601("<p1> paid <p2> <a>"),
    
    /**
     * LoggingMessage602: PlayerDepositBank
     */
    L602("<p1> deposited <a> into their Bank"),
    
    /**
     * LoggingMessage603: PlayerWithdrawBank
     */
    L603("<p1> withdrew <a> from their Bank Account"),
    
    /**
     * LoggingMessage604: PlayerWithdrawJoint
     */
    L604("<p1> withdrew <a> from Joint Account: <acc>"),
    
    /**
     * LoggingMessage605: PlayerDepositJoint
     */
    L605("<p1> deposited <a> from Joint Account: <acc>"),
    
    /**
     * LoggingMessage606: PaidJoint
     */
    L606("<p1> paid <a> to Joint Account: <acc>"),
    
    /**
     * LoggingMessage607: PlayerPaidWithJoint
     */
    L607("<p1> paid <p2> using Join Account: <acc>"),
    
    /**
     * LoggingMessage608: PlayerPFBank
     */
    L608("<p1> used PayForwarding to deposit <a> directly into their Bank"),
    
    /**
     * LoggingMessage609: PlayerPFPayWithBank
     */
    L609("<p1> used PayForwarding connected to their Bank to pay <a> to <p2> "),
    
    /**
     * LoggingMessage610: PlayerPFPayWithJoint
     */
    L610("<p1> used PayForwarding connected to 'Joint Account: <acc>' to pay <p2>"),
    
    /**
     * LoggingMessage611: CreateJoint
     */
    L611("<p1> created Joint Account: <acc>"),
    
    /**
     * LoggingMessage612: DeleteJoint
     */
    L612("<p1> deleted Joint Account: <acc>"),
    
    /**
     * LoggingMessage613: Joint-AddOwner
     */
    L613("<p1> added <p2> as 'Owner' to Joint Account: <acc>"),
    
    /**
     * LoggingMessage614: ViewTop
     */
    L614("<p1> veiwed Top <a> Richest Players"),
    
    /**
     * LoggingMessage615: Joint-AddUser
     */
    L615("<p1> added <p2> as 'User' to Joint Account: <acc>"),
    
    /**
     * LoggingMessage616: Joint-RemoveOwner
     */
    L616("<p1> removed <p2> as 'Owner' to Joint Account: <acc>"),
    
    /**
     * LoggingMessage617: Joint-RemoveUser
     */
    L617("<p1> removed <p2> as 'User' to Joint Account: <acc>"),
    
    /**
     * LoggingMessage618: PAccount-Reset
     */
    L618("<p1> reset <p2> Account to default balance"),
    
    /**
     * LoggingMessage619: PAccount-Set
     */
    L619("<p1> set <p2>'s Account to <a>"),
    
    /**
     * LoggingMessage620: PAccount-Add
     */
    L620("<p1> added <a> to <p2>'s Account"),
    
    /**
     * LoggingMessage621: PAccount-Remove
     */
    L621("<p1> removed <a> from <p2>'s Account"),
    
    /**
     * LoggingMessage622: Bank-Reset
     */
    L622("<p1> reset <p2>'s Bank to default balance"),
    
    /**
     * LoggingMessage623: Bank-Set
     */
    L623("<p1> set <p2>'s Bank Balance to <a>"),
    
    /**
     * LoggingMessage624: Bank-Add
     */
    L624("<p1> added <a> to <p2>'s Bank"),
    
    /**
     * LoggingMessage625: Bank-Remove
     */
    L625("<p1> removed <a> from <p2>'s Bank"),
    
    /**
     * LoggingMessage626: Joint-Reset
     */
    L626("<p1> reset 'Joint Account: <acc>' to default settings"),
    
    /**
     * LoggingMessage627: Joint-Set
     */
    L627("<p1> set 'Joint Account: <acc>' balance to <a>"),
    
    /**
     * LoggingMessage628: Joint-Add
     */
    L628("<p1> added <a> to Joint Account: <acc>"),
    
    /**
     * LoggingMessage629: Joint-Remove
     */
    L629("<p1> remove <a> from Joint Account: <acc>"),
    
    /**
     * LoggingMessage630: Joint-UserAdded
     */
    L630("<p1> added <p2> as Owner to Joint Account: <acc>"),
    
    /**
     * LoggingMessage631: Player-PayForwardDepositJoint
     */
    L631("<p1> used Pay Forwarding to deposit <a> into Joint Account: <acc>"),
    
    /**
     * LoggingMessage632: Player-UsedPFtoPayJointUsingJoint
     */
    L632("<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using Joint Account: <acc>"),
    
    /**
     * LoggingMessage633: Player-UsedPFtoPayJointUsingBank
     */
    L633("<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using their Bank"),
    
    /**
     * LoggingMessage634: PlayerSetPFtoJoint
     */
    L634("<p1> set Pay Forwarding to Joint Account: <acc>"),
    
    /**
     * LoggingMessage635: PlayerSetPFtoBank
     */
    L635("<p1> set Pay Forwarding to their Bank Account"),
    
    /**
     * LoggingMessage636: Player-TurnPFOff
     */
    L636("<p1> turned off Pay Forwarding"),
    
    /**
     * LoggingMessage637: Player-SetJointMaxUserWithdraw
     */
    L637("<p1> set UserMaxWithdraw to <a> for Joint Account: <acc>"),
    
    /**
     * LoggingMessage638: Player-ViewedOwnRank
     */
    L638("<p1> viewed own rank"),
    
    /**
     * LoggingMessage639: Player-ViewedAnothersRank
     */
    L639("<p1> viewed <p2>'s rank");
    
    private String mess;
    
    private LoggingMessages(String defmess){
        this.mess = defmess;
    }
    
    void setMess(String mess){
        this.mess = mess;
    }
    
    final String plainMess(){
        return mess;
    }
    
    public final String Mess(String p1, String p2, double amount, String account){
        return LoadMessages.parseLog(mess, p1, p2, String.valueOf(amount), account);
    }

}
