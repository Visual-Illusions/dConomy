package net.visualillusionsent.dconomy.messages;

public enum HelpMessages {
    
    /**
     *  HelpMessage501: Money-HelpOpen
     */
    H501("<green>|--------------<gold>dConomy /money Help<green>--------------|"),
    
    /**  
     *  HelpMessage502: RequiredOptionalAlias
     */
    H502("<green>|----<rose><REQUIRED><green>----<lightblue>[Optional]<green>----<yellow>(Alias)<green>-|"),
    
    /**  
     *  HelpMessage503: Money-Base
     */
    H503("   <gold>/money<green>: Basic Command/Display account balance"),
    
    /**
     * HelpMessage504: Money-Pay
     */
    H504("   <gold>pay <yellow>(-p) <rose><p> <a><green>: Pay Player Amount"),
    
    /**
     * HelpMessage505: Money-Rank
     */
    H505("   <gold>rank <yellow>(-r) <lightblue>[p]<green>: Display Rank Position"),
    
    /**
     * HelpMessage506: Money-Top
     */
    H506("   <gold>top <yellow>(-t) <lightblue>[a]<green>: Display Richest Players"),
    
    /**
     * HelpMessage507: Money-Set
     */
    H507("   <gold>set <yellow>(-s) <rose><p> <a><green>: Set player's account to amount"),
    
    /**
     * HelpMessage508: Money-Reset
     */
    H508("   <gold>reset <yellow>(-rt) <rose><p><green>: Reset players account to default"),
    
    /**
     * HelpMessage509: Money-Add
     */
    H509("   <gold>add <yellow>(-a) <rose><p> <a><green>: Add amount to player's account"),
    
    /**
     * HelpMessage510: Money-Remove
     */
    H510("   <gold>remove <yellow>(-rm) <rose><p> <a><green>: Remove amount from player's account"),
    
    /**
     * HelpMessage511: Money-Auto
     */
    H511("   <gold>auto <yellow>(-au) <green>: checks autodep account settings"),
    
    /**
     * HelpMessage512: Money-SetAuto
     */
    H512("   <gold>setauto <yellow>(-sa) <rose><acc|OFF><green>: Changes or sets autodep account"),
    
    /**
     * HelpMessage513: Money-UseJointHelp
     */
    H513("   <green>Use <gold>/bank ? <green>for help with Bank commands"),
    
    /**
     * HelpMessage514: Money-UseBankHelp
     */
    H514("   <green>Use <gold>/joint ? <green>for help with Joint Accounts"),
    
    /**
     * HelpMessage515: Bank-HelpOpen
     */
    H515("<green>|--------------<gold>dConomy /bank Help<green>--------------|"),
    
    /**
     * HelpMessage516: Bank-Base
     */
    H516("   <gold>/bank<green>: Basic Command/Display bank balance"),
    
    /**
     * HelpMessage517: Bank-Withdraw
     */
    H517("   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from bank account"),
    H518("   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into bank account"),
    H519("   <gold>reset <yellow>(-rt) <rose><p><green>: reset player's bank to 0"),
    H520("   <gold>set <yellow>(-s) <rose><p> <amount><green>: set player's bank to amount"),
    H521("   <gold>add <yellow>(-a) <rose><p><green>: add amount to player's bank"),
    H522("   <gold>remove <yellow>(-rm) <rose><p> <a><green>: remove amount from player's bank"),
    H523("<green>|--------------<gold>dConomy /joint Help<green>--------------|"),
    H524("   <gold>/joint <account><green>: Basic Command/Display JointAccount balance"),
    H525("   <gold>withdraw <yellow>(-w) <rose><a><green>: withdraw from Account"),
    H526("   <gold>deposit <yellow>(-d) <rose><a><green>: deposit into Account"),
    H527("   <gold>pay <yellow>(-p) <rose><p><green>: pay Account"),
    H528("   <gold>create <yellow>(-c) <green>: create Account"),
    H529("   <gold>delete <yellow>(-del) <green>: deposit into Account"),
    H530("   <gold>addowner <yellow>(-ao) <rose><p><green>: add owner to Account"),
    H531("   <gold>removeowner <yellow>(-ro) <rose><p><green>: remove owner from Account"),
    H532("   <gold>adduser <yellow>(-au) <rose><p><green>: add user to Account"),
    H533("   <gold>removeuser <yellow>(-ru) <rose><p><green>: remove user from Account"),
    H534("   <gold>setusermax <yellow>(-su) <rose><a><green>: set UserMaxWithdraw per set period"),
    H535("   <gold>reset <yellow>(-rt) <rose><a><green>: reset Account to 0"),
    H536("   <gold>set <yellow>(-s) <rose><a><green>: set Account to amount"),
    H537("   <gold>add <yellow>(-a) <rose><a><green>: add amount to Account"),
    H538("   <gold>remove <yellow>(-rm) <rose><a><green>: remove amount to Account"),
    H539("   <gold>usermax <yellow>(-um) <rose><a><green>: checks UserMaxWithdraw per set period"),
    H540("<green>|---- <p> = Player <a> = Amount <acc> = Account ----|"),
    H541("   <green>Use <gold>/money ? admin <green>for help with Money Admin commands"),
    H542("   <green>Use <gold>/bank ? admin <green>for help with Bank Admin commands"),
    H543("   <green>Use <gold>/joint ? <green>for help with Joint Admin commands"),
    H544("<green>|------------<gold>dConomy /money Admin Help<green>------------|"),
    H545("<green>|------------<gold>dConomy /bank Admin Help<green>------------|"),
    H546("<green>|------------<gold>dConomy /joint Admin Help<green>------------|");
    
    
    /**
    *H517 = parseString(H517, "517-");
    *H518 = parseString(H518, "518-Bank-Deposit");
    *H519 = parseString(H519, "519-Bank-Reset");
    *H520 = parseString(H520, "520-Bank-Set");
    *H521 = parseString(H521, "521-Bank-Add");
    *H522 = parseString(H522, "522-Bank-Remove");
    *H523 = parseString(H523, "523-Joint-HelpOpen");
    *H524 = parseString(H524, "524-Joint-Base");
    *H525 = parseString(H525, "525-Joint-Withdraw");
    *H526 = parseString(H526, "526-Joint-Deposit");
    *H527 = parseString(H527, "527-Joint-Pay");
*    H528 = parseString(H528, "528-Joint-Create");
 *   H529 = parseString(H529, "529-Joint-Delete");
    *H530 = parseString(H530, "530-Joint-AddOwner");
    *H531 = parseString(H531, "531-Joint-RemoveOwner");
    *H532 = parseString(H532, "532-Joint-AddUser");
    *H533 = parseString(H533, "533-Joint-RemoveUser");
*    H534 = parseString(H534, "534-Joint-UserMax");
    *H535 = parseString(H535, "535-Joint-Reset");
   *H536 = parseString(H536, "536-Joint-Set");
    *H537 = parseString(H537, "537-Joint-Add");
    *H538 = parseString(H538, "538-Joint-Remove");
    *H539 = parseString(H539, "539-Joint-UserMaxCheck");
    *H540 = parseString(H540, "540-Help-PlayerAmountAccount");
    *H541 = parseString(H541, "541-Help-UseMoneyAdmin");
   * H542 = parseString(H542, "542-Help-UseBankAdmin");
   * H543 = parseString(H543, "543-Help-UseJointAdmin");
   * H544 = parseString(H544, "544-Help-MoneyAdminOpen");
   * H545 = parseString(H545, "545-Help-BankAdminOpen");
   * H546 = parseString(H546, "546-Help-JointAdminOpen");
    */
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
                    
    public final String Mess(){
        return mess;
    }
}
