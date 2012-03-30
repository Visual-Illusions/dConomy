package net.visualillusionsent.dconomy.messages;

public enum AdminMessages {
    
    /**
     * AdminMessage301: AccountReset
     */
    A301("\u00A76\u00A7l<p>'s \u00A7b\u00A7l<acc>\u00A7a has been reset to \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AdminMessage302: AccountSet
     */
    A302("\u00A76\u00A7l<p>'s \u00A7b\u00A7l<acc>\u00A7a has been set to <a> <m>\u00A7a."),
    
    /**
     * AdminMessage303: AccountRemove
     */
    A303("\u00A7aYou have deducted \u00A7e<a> <m>\u00A7a from \u00A76\u00A7l<p>' \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AdminMessage304: AccountAdd
     */
    A304("\u00A7e<a> <m>\u00A7a has been added to \u00A76\u00A7l<p>'s \u00A7b\u00A7l<acc>\u00A7a."),
    
    /**
     * AdminMessage305: JointReset
     */
    A305("\u00A7a Joint Account: \u00A7b<a>'s Balance\u00A7a has been reset to 0."),
    
    /**
     * AdminMessage306: JointSet
     */
    A306("\u00A7a Joint Account: \u00A7b<acc>'s Balance\u00A7a has been set to \u00A7e<a> <m>\u00A7a."),
    
    /**
     * AdminMessage307: JointAdd
     */
    A307("\u00A7e<a> <m>\u00A7a has been added to Joint Account: \u00A7b<acc>'s Balance\u00A7a."),
    
    /**
     *AdminMessage308: JointRemove
     */
    A308("\u00A7e<a> <m>\u00A7a has been removed from Joint Account: \u00A7b<acc>'s Balance\u00A7a.");
    
    private String mess;
    
    private AdminMessages(String defmess){
        this.mess = defmess;
    }
    
    void setMess(String mess){
        this.mess = mess;
    }
    
    final String plainMess(){
        return mess;
    }
    
    public final String Mess(String username, String type, double amount){
        return LoadMessages.parseAdminMessage(mess, username, type, amount);
    }
}
