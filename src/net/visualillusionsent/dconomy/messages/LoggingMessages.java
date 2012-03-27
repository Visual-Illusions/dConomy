package net.visualillusionsent.dconomy.messages;

public enum LoggingMessages {
    
    L601("<p1> paid <p2> <a>"),
    L602("<p1> deposited <a> into their Bank"),
    L603("<p1> withdrew <a> from their Bank Account"),
    L604("<p1> withdrew <a> from Joint Account: <acc>"),
    L605("<p1> deposited <a> from Joint Account: <acc>"),
    L606("<p1> paid <a> to Joint Account: <acc>"),
    L607("<p1> paid <p2> using Join Account: <acc>"),
    L608("<p1> used PayForwarding to deposit <acc> directly into their Bank"),
    L609("<p1> used PayForwarding connected to their Bank to pay <a> to <p2> "),
    L610("<p1> used PayForwarding connected to 'Joint Account: <acc>' to pay <p2>"),
    L611("<p1> created Joint Account: <acc>"),
    L612("<p1> deleted Joint Account: <acc>"),
    L613("<p1> added <p2> as 'Owner' to Joint Account: <acc>"),
    L614("<p1> veiwed Top <a> Richest Players"),
    L615("<p1> added <p2> as 'User' to Joint Account: <acc>"),
    L616("<p1> removed <p2> as 'Owner' to Joint Account: <acc>"),
    L617("<p1> removed <p2> as 'User' to Joint Account: <acc>"),
    L618("<p1> reset <p2> Account to default balance"),
    L619("<p1> set <p2>'s Account to <a>"),
    L620("<p1> added <a> to <p2>'s Account"),
    L621("<p1> removed <a> from <p2>'s Account"),
    L622("<p1> reset <p2>'s Bank to default balance"),
    L623("<p1> set <p2>'s Bank Balance to <a>"),
    L624("<p1> added <a> to <p2>'s Bank"),
    L625("<p1> removed <a> from <p2>'s Bank"),
    L626("<p1> reset 'Joint Account: <acc>' to default settings"),
    L627("<p1> set 'Joint Account: <acc>' balance to <a>"),
    L628("<p1> added <a> to Joint Account: <acc>"),
    L629("<p1> remove <a> from Joint Account: <acc>"),
    L630("<p1> added <p2> as Owner to Joint Account: <acc>"),
    L631("<p1> used Pay Forwarding to deposit <a> into Joint Account: <acc>"),
    L632("<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using Joint Account: <acc>"),
    L633("<p1> used Pay Forwarding to pay <a> to Joint Account: <acc> using their Bank"),
    L634("<p1> set Pay Forwarding to Joint Account: <acc>"),
    L635("<p1> set Pay Forwarding to their Bank Account"),
    L636("<p1> turned off Pay Forwarding"),
    L637("<p1> set UserMaxWithdraw to <a> for Joint Account: <acc>"),
    L638("<p1> viewed own rank"),
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
    
    public final String Mess(String arg){
        return LoadMessages.parseError(mess, arg);
    }

}
