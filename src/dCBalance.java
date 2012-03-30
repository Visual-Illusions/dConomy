import net.visualillusionsent.dconomy.AccountType;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.data.DataSource;

/**
 * Handles custom hooks for interfacing with dConomy
 * <p>
 * Using hooks requires calling etc.getLoader().callCustomHook("dCBalance", new Object({Command, args...});
 * For the different returns you will need to cast it to the appropriate type  ie: (String)loader.callCustomHook("dCBalance", new Object({"MoneyName"});
 * <p>
 * <table border="1">
 * <colgroup><col width="10%" /><col width="35%" /><col width="35%" /><col width="20%" /></colgroup>
 * <tr><th>Command</th><th>Description</th><th>Arguments</th><th>Returns</th></tr>
 * <tr><td>MoneyName</td><td>Gets the name of the Currency</td><td>N/A</td><td>String <p>Currency Name</p></td></tr>
 * <tr><td>Account-Balance</td><td>Get's player's account balance</td><td>(String)Player's Name</td><td>(Double) Balance (-1 if not found)</td></tr>
 * <tr><td>Bank-Balance</td><td>Get's the player's bank balance</td><td>(String)Player's Name</td><td>(Double) Balance (-1 if not found)</td></tr>
 * <tr><td>Account-Deposit</td><td>Deposits money into the Player's Account</td><td>(String)Player's Name, (Double)Amount to deposit</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Bank-Deposit</td><td>Deposit Money into Player's Bank</td><td>(String)Player's Name, (Double)Amount to deposit</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Account-Withdraw</td><td>Withdraws Money from Player's Account</td><td>(String)Player's Name, (Double)Amount to be withdrawn</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Bank-Withdraw</td><td>Withdraw Money from Player's Bank</td><td>(String)Player's Name, (Double)Amount to be withdrawn</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Joint-Balance-NC</td><td>Check Joint Account Balance - Will not check owner/user permissions</td><td>(String)Account Name</td><td>(Double) Balance (-1 if not found)</p></td></tr>
 * <tr><td>Joint-Balance</td><td>Check Joint Account Balance - Will check permissions</td><td>(String)Player's Name, (String)Account Name</td><td>(Double) Balance (-1 if not found, -2 if player has no permission)</td></tr>
 * <tr><td>Joint-Deposit-NC</td><td>Deposit Money into Joint Account - Will not check owner/user permissions</td><td>(String)Account Name, (Double)Amount to deposit</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Joint-Deposit</td><td>Deposit Money into Joint Account - Will check owner/user permissions</td><td>(String)Player's Name, (String)Account Name, (Double)Amount to deposit</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Joint-Withdraw-NC</td><td>Withdraws Money from Joint Account - Will not check owner/user permissions</td><td>(String)Account Name, (Double)Amount to withdraw</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Joint-Withdraw</td><td>Withdraws Money from Joint Account - Will check owner/user permissions</td><td>(String)Player's Name, (String)Account Name, (Double)Amount to withdraw</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Player-Pay</td><td>Give Money to a Player - performs all pay forwarding checks</td><td>(String)Player's Name, (Double)Amount to pay</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Player-Charge</td><td>Charge a Player money - performs all pay forwarding checks</td><td>(String)Player's Name, (Double)Amount to charge</td><td>(Double) New Balance (-1 if failed)</td></tr>
 * <tr><td>Player-Balance</td><td>Checks a Players pay forwarded account balance</td><td>(String)Player's Name</td><td>(Double) Balance (-1 if not found)</td></tr>
 * <tr><td>Log</td><td>Logs transaction to the dConomy Log if logging is enabled</td><td>(String) Log Message</td><td>N/A</td></tr>
 * </table>
 * 
 * @since    2.0
 * @author   darkdiplomat
 */
public class dCBalance implements PluginInterface{
    private DataSource ds = DCoProperties.getDS();
    private AccountType ACC = AccountType.ACCOUNT;
    private AccountType BAK = AccountType.BANK;
    private AccountType JON = AccountType.JOINT;
        
    public String getName(){
        return "dCBalance";
    }
    
    public int getNumParameters() {
        return 4;
    }
    
    public String checkParameters(Object[] os) {
        if ((os.length < 1) || (os.length > getNumParameters())) {
            return "Invalid amount of parameters.";
        }
        return null;
    }
    
    public Object run(Object[] os) {
        String cmd = (String)os[0];
        
        if(cmd.equalsIgnoreCase("MoneyName")){
            return DCoProperties.getMoneyName();
        }
        else if(cmd.equalsIgnoreCase("Account-Balance")){
            String accname = (String)os[1];
            double balance = -1;
            if (ds.AccountExists(ACC, accname)){
                balance = ds.getBalance(ACC, accname);
            }
            return balance;
        }
        else if (cmd.equalsIgnoreCase("Bank-Balance")){
            String accname = (String)os[1];
            double balance = -1;
            if (ds.AccountExists(BAK, accname)){
                balance = ds.getBalance(BAK, accname);
            }
            return balance;
        }
        else if (cmd.equalsIgnoreCase("Account-Deposit")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(ACC, accname)){
                if (amount > 0.01){
                    newBal = ds.getBalance(ACC, accname) + amount;
                    ds.setBalance(ACC, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Bank-Deposit")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(BAK, accname)){
                if (amount > 0.01){
                    newBal = ds.getBalance(BAK, accname) + amount;
                    ds.setBalance(BAK, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Account-Withdraw")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(ACC, accname)){
                if (amount > 0.01){
                    newBal = ds.getBalance(ACC, accname) - amount;
                    newBal = (newBal >= 0 ? newBal : 0);
                    ds.setBalance(ACC, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Bank-Withdraw")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(BAK, accname)){
                if ((Double)os[2] > 0){
                    newBal = ds.getBalance(BAK, accname) - amount;
                    newBal = (newBal >= 0 ? newBal : 0);
                    ds.setBalance(BAK, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Joint-Balance-NC")){
            String accname = (String)os[1];
            double balance = -1;
            if (ds.AccountExists(JON, accname)){
                balance = ds.getBalance(JON, accname);
            }
            return balance;
        }
        else if (cmd.equalsIgnoreCase("Joint-Balance")){
            String pname = (String)os[1];
            String accname = (String)os[2];
            double balance = -1;
            if (ds.AccountExists(JON, accname)){
                if (ds.isJointUser(accname, pname)){
                    balance = ds.getBalance(JON, accname);
                }else{
                    balance = -2;
                }
            }
            return balance;
        }
        else if (cmd.equalsIgnoreCase("Joint-Deposit-NC")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(JON, accname)){
                if (amount > 0){
                    newBal = ds.getBalance(JON, accname) + amount;
                    ds.setBalance(JON, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Joint-Deposit")){
            String pname = (String)os[1];
            String accname = (String)os[2];
            double amount = (Double)os[3];
            double newBal = -1;
            if (ds.AccountExists(JON, accname)){
                if (ds.isJointUser(accname, pname)){
                    if(amount > 0){
                        newBal = ds.getBalance(JON, accname) + amount;
                        ds.setBalance(JON, accname, newBal);
                    }
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Joint-Withdraw-NC")){
            String accname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (ds.AccountExists(JON, accname)){
                if (amount > 0){
                    newBal = ds.getBalance(JON, accname) - amount;
                    newBal = (newBal >= 0 ? newBal : 0);
                    ds.setBalance(JON, accname, newBal);
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Joint-Withdraw")){
            String pname = (String)os[1];
            String accname = (String)os[2];
            double amount = (Double)os[3];
            double newBal = -1;
            if (ds.AccountExists(JON, accname)){
                if (amount > 0){
                    if (ds.isJointUser(accname, pname)){
                        if(!DCoProperties.getDS().isJointOwner(accname, pname)){
                            if(DCoProperties.getDS().canWithdraw(pname, accname, amount)){
                                DCoProperties.getDS().addJUWD(pname, accname, amount);
                                newBal = ds.getBalance(JON, accname) - amount;
                                newBal = (newBal >= 0 ? newBal : 0);
                                ds.setBalance(JON, accname, newBal);
                            }
                        }
                        else{
                            newBal = ds.getBalance(JON, accname) - amount;
                            newBal = (newBal >= 0 ? newBal : 0);
                            ds.setBalance(JON, accname, newBal);
                        }
                    }
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Player-Pay")){
            String pname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (amount > 0){
                String pacc = "Account";
                if(ds.isPayForwarding(pname)){
                    pacc = ds.getPayForwardingAcc(pname);
                }
                if(pacc.equals("Account")){
                    if (ds.AccountExists(ACC, pname)){
                        newBal = ds.getBalance(ACC, pname) + amount;
                        ds.setBalance(ACC, pname, amount);
                    }
                }
                else if(pacc.equals("Bank")){
                    if(ds.AccountExists(BAK, pname)){
                        newBal = ds.getBalance(BAK, pname) + amount;
                        ds.setBalance(BAK, pname, amount);
                    }
                }
                else if(ds.AccountExists(JON, pacc)){
                    if(ds.isJointUser(pacc, pname)){
                        newBal = ds.getBalance(JON, pacc) + amount;
                        ds.setBalance(JON, pacc, amount);
                    }
                }
            }
            return newBal;
        }
        else if (cmd.equalsIgnoreCase("Player-Charge")){
            String pname = (String)os[1];
            double amount = (Double)os[2];
            double newBal = -1;
            if (amount > 0){
                String pacc = "Account";
                if(ds.isPayForwarding(pname)){
                    pacc = ds.getPayForwardingAcc(pname);
                }
                if(pacc.equals("Account")){
                    if (ds.AccountExists(ACC, pname)){
                        newBal = ds.getBalance(ACC, pname) - amount;
                        newBal = (newBal >= 0 ? newBal : 0);
                        ds.setBalance(ACC, pname, amount);
                    }
                }
                else if(pacc.equals("Bank")){
                    if(ds.AccountExists(BAK, pname)){
                        newBal = ds.getBalance(BAK, pname) - amount;
                        newBal = (newBal >= 0 ? newBal : 0);
                        ds.setBalance(BAK, pname, amount);
                    }
                }
                else if(ds.AccountExists(JON, pacc)){
                    if(ds.isJointUser(pacc, pname)){
                        if(!DCoProperties.getDS().isJointOwner(pacc, pname)){
                            if(DCoProperties.getDS().canWithdraw(pname, pacc, amount)){
                                DCoProperties.getDS().addJUWD(pname, pacc, amount);
                                newBal = ds.getBalance(JON, pacc) - amount;
                                newBal = (newBal >= 0 ? newBal : 0);
                                ds.setBalance(JON, pacc, newBal);
                            }
                        }
                        else{
                            newBal = ds.getBalance(JON, pacc) - amount;
                            newBal = (newBal >= 0 ? newBal : 0);
                            ds.setBalance(JON, pacc, amount);
                        }
                    }
                }
            }
            return newBal;
        }
        else if (cmd.equals("Player-Balance")){
            String pname = (String)os[1];
            double balance = -1;
            String pacc = "Account";
            if(ds.isPayForwarding(pname)){
                pacc = ds.getPayForwardingAcc(pname);
            }
            if(pacc.equals("Account")){
                if (ds.AccountExists(ACC, pname)){
                    balance = ds.getBalance(ACC, pname);
                }
            }
            else if(pacc.equals("Bank")){
                if(ds.AccountExists(BAK, pname)){
                    balance = ds.getBalance(BAK, pname);
                }
            }
            else if(ds.AccountExists(JON, pacc)){
                if(ds.isJointUser(pacc, pname)){
                    balance = ds.getBalance(JON, pacc);
                }
            }
            return balance;
        }
        else if (cmd.equalsIgnoreCase("Log")){
            ds.logTrans((String)os[1]);
        }
        return null;
    }
}
