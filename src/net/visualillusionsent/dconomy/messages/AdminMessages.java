package net.visualillusionsent.dconomy.messages;

public enum AdminMessages {
    
    /**
     * AdminMessage401: RankTop");
     */
    A401("\u00A72-----Top \u00A7e<rank> \u00A72Richest Players-----"),
    
    /**
     * AdminMessage402: RankSorting
     */
    A402("\u00A72   <rank>. \u00A76<p> \u00A72[\u00A7e<a> <m>\u00A72]"),
    
    /**
     * AdminMessage403: RankNoTop
     */
    A403("\u00A7c   No Top Players Yet!"),
    
    /**
     * AdminMessage404: PlayerReset
     */
    A404("\u00A76<p>'s Account\u00A7a has been reset to default balance."),
    
    /**
     * AdminMessage405: PlayerSet
     */
    A405("\u00A76<p>'s Account\u00A7a has been set to <a> <m>\u00A7a."),
    
    /**
     * AdminMessage406: PlayerRemove
     */
    A406("\u00A7aYou have deducted \u00A7e<a> <m>\u00A7a from \u00A76<p>' Account\u00A7a."),
    
    /**
     * AdminMessage407: PlayerAdd
     */
    A407("\u00A7e<a> <m>\u00A7a has been added to \u00A76<p>'s Account\u00A7a."),
    
    /**
     * AdminMessage408: BankReset
     */
    A408("\u00A76<p>'s Bank\u00A7a has been reset to 0."),
    
    /**
     * AdminMessage409: BankSet
     */
    A409("\u00A76<p>'s Bank\u00A7a has been set to <a> <m>\u00A7a."),
    
    /**
     * AdminMessage410: BankRemove
     */
    A410("\u00A7aYou have removed \u00A7e<a> <m>\u00A7a from \u00A76<p>' Bank\u00A7a."),
    
    /**
     * AdminMessage411: BankAdd
     */
    A411("\u00A7e<a> <m>\u00A7a has been added to \u00A76<p>'s Bank\u00A7a."),
    
    /**
     * AdminMessage412: JointReset
     */
    A412("\u00A7a Joint Account: \u00A7b<a>'s Balance\u00A7a has been reset to 0."),
    
    /**
     * AdminMessage413: JointSet
     */
    A413("\u00A7a Joint Account: \u00A7b<acc>'s Balance\u00A7a has been set to \u00A7e<a> <m>\u00A7a."),
    
    
    /**
     * AdminMessage414: Joint-Add
     */
    A414("\u00A7e<a> <m>\u00A7a has been added to Joint Account: \u00A7b<acc>'s Balance\u00A7a."),
    
    /*
     *AdminMessage415: JointRemove
     */
    A415("\u00A7e<a> <m>\u00A7a has been removed from Joint Account: \u00A7b<acc>'s Balance\u00A7a.");
    
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
        return LoadMessages.parseMessage(mess, username, type, amount);
    }
}
