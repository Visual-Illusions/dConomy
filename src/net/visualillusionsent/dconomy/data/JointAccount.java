package net.visualillusionsent.dconomy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * JointAccount - Handles data for Joint Accounts
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class JointAccount {
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayList<String> owners = new ArrayList<String>();
    private HashMap<String, Double> juwd = new HashMap<String, Double>();
    private double balance, muw;
    private int delay = 0;
    private long reset = -1;
    private Timer juwdt = new Timer();
    
    JointAccount(String[] users, String[] owners, double balance, double muw, int delay, long reset){
        for(String user : users){
            this.users.add(user);
        }
        for(String owner : owners){
            this.owners.add(owner);
        }
        this.balance = balance;
        this.muw = muw;
        if(DCoProperties.getJointDelay() > 0){
            if(delay > 0){
                this.delay = delay;
                long tempdelay = delay*1000*60;
                this.reset = reset;
                long tempreset = reset - System.currentTimeMillis();
                if(tempreset <= 0){
                    tempreset = this.delay;
                }
                else{
                    //TODO load juwdmap
                }
                juwdt.scheduleAtFixedRate(new Reset(), tempreset, tempdelay);
            }
        }
    }
    
    /**
     * Checks is user is an owner.
     * 
     * @param name The user's name.
     * @return true if they are
     */
    protected boolean isOwner(String name){
        return owners.contains(name);
    }
    
    /**
     * Adds a user as an owner.
     * 
     * @param name The user's name.
     */
    protected void addOwner(String name){
        if(!owners.contains(name)){
            owners.add(name);
        }
    }
    
    /**
     * Removes user as an owner.
     * 
     * @param name The user's name.
     */
    protected void removeOwner(String name){
        if(owners.contains(name)){
            owners.remove(name);
        }
    }
    
    /**
     * Gets the account's owners
     * 
     * @return the owners
     */
    protected String getOwners(){
        synchronized(owners){
            StringBuilder sb = new StringBuilder();
            if(!owners.isEmpty()){
                for(String own : owners){
                    sb.append(own);
                    sb.append(",");
                }
            }
            else{
                sb.append(",");
            }
            return sb.toString();
        }
    }
    
    /**
     * Checks is user is an user.
     * 
     * @param name The user's name.
     * @return true if they are an user or an owner
     */
    protected boolean isUser(String name){
        return users.contains(name) || owners.contains(name);
    }
    
    /**
     * Checks is user is an user.
     * 
     * @param name The user's name.
     * @return true if they are an user
     */
    protected boolean isAbsolueUser(String name){
        return users.contains(name);
    }
    
    /**
     * Adds a user as an user.
     * 
     * @param name The user's name.
     */
    protected void addUser(String name){
        if(!users.contains(name)){
            users.add(name);
        }
    }
    
    /**
     * Removes user as an user.
     * 
     * @param name The user's name.
     */
    protected void removeUser(String name){
        if(users.contains(name)){
            users.remove(name);
        }
    }
    
    /**
     * Gets the account's users
     * 
     * @return the users
     */
    protected String getUsers(){
        synchronized(users){
            StringBuilder sb = new StringBuilder();
            if(!users.isEmpty()){
                for(String user : users){
                    sb.append(user);
                    sb.append(",");
                }
            }
            else{
                sb.append(",");
            }
            return sb.toString();
        }
    }
    
    /**
     * Gets the account's balance
     * 
     * @return balance
     */
    protected double getBalance(){
        return balance;
    }
    
    /**
     * Sets the accounts balance
     * 
     * @param bal
     */
    protected void setBalance(double bal){
        balance = bal;
    }
    
    /**
     * Gets the max user withdraw
     * 
     * @return muw
     */
    protected double getMaxUserWithdraw(){
        return muw;
    }
    
    /**
     * Sets the max user withdraw
     * 
     * @param max
     */
    protected void setMaxUserWithdraw(double max){
        this.muw = max;
    }
    
    /**
     * 
     * @param username
     * @param amount
     */
    protected void addJUWD(String username, double amount){
        juwd.put(username, amount);
    }
    
    /**
     * Cancels the JointWithdrawDelay timer
     */
    protected void cancelDelay(){
        juwdt.cancel();
        juwdt.purge();
    }
    
    protected boolean canWithdraw(String username, double amount){
        if(amount > muw){
            return false;
        }
        if(juwd.containsKey(username)){
            if(juwd.get(username) + amount > muw){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the Joint Withdraw delay
     * 
     * @return delay
     */
    protected int getDelay(){
        return delay;
    }
    
    protected void setDelay(int delay){
        this.delay = delay;
        juwdt.cancel();
        if(delay > 0){
            juwdt = new Timer();
            juwdt.scheduleAtFixedRate(new Reset(), (reset-System.currentTimeMillis()), (delay*1000*60));
        }
    }
    
    /**
     * Gets the reset time of the Joint Withdraw delay
     * 
     * @return reset
     */
    protected long getReset(){
        return reset;
    }
    
    private class Reset extends TimerTask{
        public void run(){
            if(reset > System.currentTimeMillis()){
                reset = System.currentTimeMillis() + (delay * 1000 * 60);
                setDelay(delay);
                return;
            }
            juwd.clear();
            reset = System.currentTimeMillis() + (delay * 1000 * 60);
        }
    }
}
