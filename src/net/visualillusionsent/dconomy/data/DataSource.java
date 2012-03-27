package net.visualillusionsent.dconomy.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import net.visualillusionsent.dconomy.AccountType;

/**
 * DataSouce.java - account data handling class
 * 
 * @author darkdiplomat
 *          <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 * @version 2.0
 */
public class DataSource {
    Logger logger = Logger.getLogger("Minecraft");
    HashMap<String, Double> accmap = new HashMap<String, Double>();
    HashMap<String, Double> bankmap = new HashMap<String, Double>();
    HashMap<String, JointAccount> jointmap = new HashMap<String, JointAccount>();
    HashMap<String, String> payforwardmap = new HashMap<String, String>();
    
    Date date;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * Loads Account data
     * 
     * @since dConomy v2.0
     */
    void loadMaps(){ }
    
    /**
     * Saves Account data
     * 
     * @since dConomy v2.0
     */
    public void saveMaps(){ }
    
    void ScheduleForUpdate(){ }
    
    public void logTrans(String action){ }
    
    /**
     * Checks account existance
     * 
     * @param type The type of account
     * @param accname The name/user of the account
     * @return true if exists, false otherwise
     * 
     * @since dConomy v2.0
     */
    public boolean AccountExists(AccountType type, String accname){
        if(type.equals(AccountType.ACCOUNT)){
            return accmap.containsKey(accname);
        }
        else if(type.equals(AccountType.BANK)){
            return bankmap.containsKey(accname);
        }
        else if(type.equals(AccountType.JOINT)){
            return jointmap.containsKey(accname);
        }
        return false;
    }
    
    /**
     * Checks if user is one of the Joint Account's users
     * 
     * @param accname Name of the Joint Account
     * @param username Name of the user
     * @return true is user, false otherwise
     * 
     * @since dConomy v2.0
     */
    public boolean isJointUser(String accname, String username){
        return jointmap.get(accname).isUser(username);
    }
    
    /**
     * Checks if user is one of the Joint Account's owner
     * 
     * @param accname Name of the Joint Account
     * @param ownername Name of the user
     * @return true if owner, false otherwise
     * 
     * @since dConomy v2.0
     */
    public boolean isJointOwner(String accname, String ownername){
        return jointmap.get(accname).isOwner(ownername);
    }
    
    /**
     * Gets the balance for the account
     * 
     * @param type The type of account
     * @param accname The account name/user
     * @return balance
     * 
     * @since dConomy v2.0
     */
    public double getBalance(AccountType type, String accname){
        if(type.equals(AccountType.ACCOUNT)){
            return accmap.get(accname);
        }
        else if(type.equals(AccountType.BANK)){
            return bankmap.get(accname);
        }
        else if(type.equals(AccountType.JOINT)){
            return jointmap.get(accname).getBalance();
        }
        return 0;
    }
    
    /**
     * Gets the Joint Account's maximum user withdraw amount
     * 
     * @param accname The name of the joint account
     * @return the amount of the maximum withdraw
     * 
     * @since dConomy v2.0
     */
    public double getJointUserWithdrawMax(String accname){
        return jointmap.get(accname).getMaxUserWithdraw();
    }
    
    /**
     * Sets the Account's balance
     * 
     * @param type The type of account
     * @param accname The account name/user
     * @param bal The new balance amount
     * 
     * @since dConomy v2.0
     */
    public void setBalance(AccountType type, String accname, double bal){
        if(type.equals(AccountType.ACCOUNT)){
            accmap.put(accname, bal);
        }
        else if(type.equals(AccountType.BANK)){
            bankmap.put(accname, bal);
        }
        else if(type.equals(AccountType.JOINT)){
            jointmap.get(accname).setBalance(bal);
        }
    }
    
    /**
     * Sets the starting balance for the account
     * 
     * @param type The type of account
     * @param accname The account name/user
     * 
     * @since dConomy v2.0
     */
    public void setInitialBalance(AccountType type, String accname){
        if(type.equals(AccountType.ACCOUNT)){
            accmap.put(accname, DCoProperties.getInitialBalance());
        }
        else if(type.equals(AccountType.BANK)){
            bankmap.put(accname, 0d);
        }
        else if(type.equals(AccountType.JOINT)){
            jointmap.get(accname).setBalance(0);
        }
    }
    
    /**
     * Creates a joint account of the given name and sets the owner to the given owner
     * 
     * @param accname The name of the account to create
     * @param owner The name of the owner for the account
     * 
     * @since dConomy v2.0
     */
    public void createJointAccount(String accname, String owner){
        jointmap.put(accname, new JointAccount(null, new String[]{owner}, 0, DCoProperties.getJointMaxDraw()));
    }
    
    /**
     * Deletes a Joint account
     * 
     * @param accname The name of the Joint account
     * 
     * @since dConomy v2.0
     */
    public void deleteJointAccount(String accname){
        jointmap.remove(accname);
    }
    
    /**
     * Adds a Owner to the Joint Account
     * 
     * @param accname The name of the Joint account
     * @param owner The name of the owner
     * 
     * @since dConomy v2.0
     */
    public void addJointOwner(String accname, String owner){
        jointmap.get(accname).addOwner(owner);
    }
    
    public void removeJointOwner(String accname, String owner){
        jointmap.get(accname).removeOwner(owner);
    }
    
    public void setJointMaxUserWithdraw(String accname, double maxamount){
        
    }
    
    public void LogTransaction(String action){ }
    
    public String getJointUsers(String accname){
        return null;
    }
    
    public String getJointOwners(String name){
        return null;
    }
    
    public boolean isPayForwarding(String username){
        return payforwardmap.containsKey(username);
    }
    
    public String getPayForwardingAcc(String username){
        return payforwardmap.get(username);
    }
    
    public boolean canWithdraw(String username, String accname, double amount){
        return jointmap.get(accname).canWithdraw(username, amount);
    }
}
