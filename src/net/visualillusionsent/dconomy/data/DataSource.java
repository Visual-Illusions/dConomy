package net.visualillusionsent.dconomy.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.visualillusionsent.dconomy.AccountType;

/**
 * DataSouce.java - account data handling class
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class DataSource {
    Logger logger = Logger.getLogger("Minecraft");
    HashMap<String, Double> accmap = new HashMap<String, Double>();
    HashMap<String, Double> bankmap = new HashMap<String, Double>();
    HashMap<String, JointAccount> jointmap = new HashMap<String, JointAccount>();
    Properties payforwardmap = new Properties();
    
    Date date;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    
    long jreset = 0L, breset = 0L;
    
    private ScheduledThreadPoolExecutor stpe;
    Properties reseter = new Properties();
    private final File resetFile = new File("plugins/config/dConomy/dCTimerReser.DONOTEDIT");
    
    /**
     * Sets up Saving and Bank Interest
     * 
     * @since 2.0
     */
    void Scheduler(){
        long btemp = 0;
        if(DCoProperties.getBankDelay() > 0){
            if(resetFile.exists()){
                try {
                    FileInputStream in = new FileInputStream(resetFile);
                    reseter.load(in);
                    in.close();
                } 
                catch (IOException e) { }
                if(reseter.containsKey("BankTimerResetTo")){
                    breset = Long.valueOf(reseter.getProperty("BankTimerResetTo")) - System.currentTimeMillis();
                }
                else{
                    breset = DCoProperties.getBankDelay();
                }
            }
            else{
                try {
                    resetFile.createNewFile();
                }
                catch (IOException e) { }
                breset = DCoProperties.getBankDelay();
            }
            if(breset > 0){
                btemp = (long)(breset / 60 / 1000);
            }
        }
        
        stpe = new ScheduledThreadPoolExecutor(1);
        stpe.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        stpe.scheduleAtFixedRate(new SaveCaller(), 15L, 15L, TimeUnit.MINUTES);
        
        if(btemp > 0){
            stpe.scheduleAtFixedRate(new BankInterestCaller(), btemp, (long)DCoProperties.getBankDelay(), TimeUnit.MINUTES);
        }
    }
    
    /**
     * Calls the termination of threads
     * 
     * @since 2.0
     */
    public void terminateThreads(){
        stpe.shutdownNow();
        synchronized(jointmap){
            for(JointAccount joint : jointmap.values()){
                joint.cancelDelay();
            }
        }
    }
    
    /**
     * loads account data
     * 
     * @since   2.0
     */
    boolean loadMaps(){
        try {
            FileInputStream in = new FileInputStream("plugins/config/dConomy/PayForwarding.txt");
            payforwardmap.load(in);
            in.close();
        } 
        catch (IOException e) { }
        return true;
    }
    
    /**
     * saves account data
     * 
     * @since   2.0
     */
    public void saveMaps(){
        try {
            FileOutputStream out = new FileOutputStream("plugins/config/dConomy/PayForwarding.txt");
            payforwardmap.store(out, null);
            out.close();
            
            out = new FileOutputStream(resetFile);
            reseter.store(out, null);
            out.close();
        }
        catch (IOException e) { }
    }
    
    /**
     * Logs a transaction
     * 
     * @param action The action to be logged
     * 
     * @since 2.0
     */
    public void logTrans(String action){ }
    
    /**
     * Checks account existance
     * 
     * @param type The type of account
     * @param accname The name/user of the account
     * @return true if exists, false otherwise
     * 
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
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
     * @since   2.0
     */
    public void createJointAccount(String accname, String owner){
        jointmap.put(accname, new JointAccount(null, new String[]{owner}, 0, DCoProperties.getJointMaxDraw(), DCoProperties.getJointDelay(), DCoProperties.getJointDelay()));
    }
    
    /**
     * Deletes a Joint account
     * 
     * @param accname The name of the Joint account
     * 
     * @since   2.0
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
     * @since   2.0
     */
    public void addJointOwner(String accname, String owner){
        jointmap.get(accname).addOwner(owner);
    }
    
    /**
     * Removes a Owner to the Joint Account
     * 
     * @param accname The name of the Joint account
     * @param owner The name of the owner
     * 
     * @since   2.0
     */
    public void removeJointOwner(String accname, String owner){
        jointmap.get(accname).removeOwner(owner);
    }
    
    
    public void setJointMaxUserWithdraw(String accname, double maxamount){
        
    }
    
    public String getJointUsers(String accname){
        return null;
    }
    
    /**
     * Gets the owners for a Joint Account
     * 
     * @param name The name of the Joint Account to get owners for
     * @return the names of the owners
     * 
     * @since 2.0
     */
    public String getJointOwners(String name){
        return jointmap.get(name).getOwners();
    }
    
    /**
     * Checks is a user has Pay Forwarding active
     * 
     * @param username The name of the user to check
     * @return true if user has Pay Forwarding active
     * 
     * @since 2.0
     */
    public boolean isPayForwarding(String username){
        if(payforwardmap.containsKey(username)){
            String acc = payforwardmap.getProperty(username);
            return !acc.equalsIgnoreCase("Account");
        }
        return false;
    }
    
    /**
     * Gets the account the user is forwarding to
     * 
     * @param username The name of the user to get account for
     * @return the account name
     * 
     * @since 2.0
     */
    public String getPayForwardingAcc(String username){
        return payforwardmap.getProperty(username);
    }
    
    public void setPayForwarding(String username, String account){
        payforwardmap.setProperty(username, account);
    }
    
    /**
     * Checks if a user can withdraw from the Joint Account
     * 
     * @param username The name of the user to check.
     * @param accname The name of the account to check.
     * @param amount  The amount of the withdraw amount to check.
     * @return true if user can
     * 
     * @since 2.0
     */
    public boolean canWithdraw(String username, String accname, double amount){
        return jointmap.get(accname).canWithdraw(username, amount);
    }
    
    /**
     * Gets a TreeMap of specified accounts in order of richest to not rich
     * 
     * @param type The account type to get a map for
     * @return the map of ordered accounts
     * 
     * @since 2.0
     */
    public Map<String, Double> getRankMap(AccountType type){
        TreeMap<String, Double> sortedAccounts = null;
        dCValueComparator bvc = null;

        try {
            if(type.equals(AccountType.ACCOUNT)){
                bvc = new dCValueComparator(accmap);
                sortedAccounts = new TreeMap<String, Double>(bvc);
                sortedAccounts.putAll(accmap);
            }
            else if (type.equals(AccountType.BANK)){
                bvc = new dCValueComparator(bankmap);
                sortedAccounts = new TreeMap<String, Double>(bvc);
                sortedAccounts.putAll(bankmap);
            }
        } catch (Exception ex) {
            logger.warning("[dConomy] Unable to retrieve array of balances!");
            return null;
        }
        return sortedAccounts;
    }
}
