package net.visualillusionsent.dconomy.data;

import java.util.ArrayList;
import java.util.HashMap;

public class JointAccount {
    private ArrayList<String> users = new ArrayList<String>();
    private ArrayList<String> owners = new ArrayList<String>();
    private HashMap<String, Double> juwd = new HashMap<String, Double>();
    private double balance, muw;
    
    JointAccount(String[] users, String[] owners, double balance, double muw){
        if(users != null){
            for(String user : users){
                this.users.add(user);
            }
        }
        for(String owner : owners){
            this.owners.add(owner);
        }
        this.balance = balance;
        this.muw = muw;
    }
    
    protected boolean isOwner(String name){
        return owners.contains(name);
    }
    
    protected void addOwner(String name){
        if(!owners.contains(name)){
            owners.add(name);
        }
    }
    
    protected void removeOwner(String name){
        if(owners.contains(name)){
            owners.remove(name);
        }
    }
    
    protected String getOwners(){
        synchronized(owners){
            StringBuilder sb = new StringBuilder();
            for(String own : owners){
                sb.append(own);
                sb.append(",");
            }
            return sb.toString();
        }
    }
    
    protected boolean isUser(String name){
        return users.contains(name) || owners.contains(name);
    }
    
    protected void addUser(String name){
        if(!users.contains(name)){
            users.add(name);
        }
    }
    
    protected void removeUser(String name){
        if(users.contains(name)){
            users.remove(name);
        }
    }
    
    protected String getUsers(){
        synchronized(users){
            StringBuilder sb = new StringBuilder();
            for(String user : users){
                sb.append(user);
                sb.append(",");
            }
            return sb.toString();
        }
    }
    
    protected double getBalance(){
        return balance;
    }
    
    protected void setBalance(double bal){
        balance = bal;
    }
    
    protected double getMaxUserWithdraw(){
        return muw;
    }
    
    protected void addJUWD(String username, double amount){
        
    }
    
    protected void clearJUWD(){
        juwd.clear();
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
}
