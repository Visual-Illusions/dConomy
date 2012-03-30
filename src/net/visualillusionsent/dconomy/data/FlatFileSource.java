package net.visualillusionsent.dconomy.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

/**
 * FlatFileSource - Class for handling FlatFile data
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since 2.0
 * @author darkdiplomat
 * 
 * @see DataSource
 */
public class FlatFileSource extends DataSource{
    private String jds = DCoProperties.getDir()+"JointAccounts/";
    private String TransLoc = DCoProperties.getDir()+/*date(day/month/year)*/"%s.txt";
    private File accFile = new File(DCoProperties.getDir()+"dCAccounts.txt");
    private File bankFile = new File(DCoProperties.getDir()+"dCBanks.txt");
    private File jointdir = new File(jds);
   
    
    FlatFileSource(){ }
    
    boolean loadMaps() {
        super.loadMaps();
        if(accFile.exists()){
            logger.info("[dConomy] Attempting to Accounts...");
            if(!loadAccounts()){
                return false;
            }
            logger.info("[dConomy] Accounts loaded!");
        }
        else{
            logger.info("[dConomy] File: 'dCAccounts.txt' not found! Attempting to create...");
            try {
                accFile.createNewFile();
                logger.info("[dConomy] File: 'dCAccounts.txt' created!");
            } 
            catch (IOException ioe) {
                logger.severe("Failed to create File: dCAccounts.txt... dConomy will now be disabled...");
                return false;
            }
        }
        if(bankFile.exists()){
            logger.info("[dConomy] Attempting to load Bank Accounts...");
            if(!loadBanks()){
                return false;
            }
            logger.info("[dConomy] Bank Accounts loaded!");
        }
        else{
            logger.info("[dConomy] File: 'dCBanks.txt' not found! Attempting to create...");
            try{
                bankFile.createNewFile();
                logger.info("[dConomy] File: 'dCBanks.txt' created!");
            } 
            catch(IOException ioe){
                logger.severe("Failed to create File: dCBanks.txt... dConomy will now be disabled...");
                return false;
            }
        }
        if(jointdir.isDirectory()){
            loadJointAccounts();
        }
        else if(!jointdir.mkdir()){
            logger.severe("Failed to create Directory: dConomy/JointAccounts/... dConomy will now be disabled...");
            return false;
        }
        Scheduler();
        return true;
    }
    
    private boolean loadAccounts(){
        synchronized(accmap){
            try{
                BufferedReader in = new BufferedReader(new FileReader(accFile));
                String line;
                String[] acc = new String[]{ "Unknown(null?)" };
                while((line = in.readLine()) != null){
                    if(line.startsWith("#")) continue;
                    try{
                        acc = line.split("=");
                        double bal = Double.parseDouble(acc[1]);
                        accmap.put(acc[0], bal);
                    }
                    catch(ArrayIndexOutOfBoundsException aioobe){
                        logger.warning("Error reading line for Account: "+acc[0]);
                        continue;
                    }
                    catch(NumberFormatException nfe){
                        logger.warning("Error reading line for Account: "+acc[0]);
                        continue;
                    }
                }
            }
            catch(IOException ioe){
                logger.log(Level.SEVERE, "Error reading File: dCAccounts.txt", ioe);
                logger.severe("dConomy will now be disabled");
                return false;
            }
        }
        return true;
    }
    
    private boolean loadBanks(){
        synchronized(bankmap){
            try{
                BufferedReader in = new BufferedReader(new FileReader(bankFile));
                String line;
                String[] acc = new String[]{ "" };
                while((line = in.readLine()) != null){
                    if(line.startsWith("#")) continue;
                    try{
                        acc = line.split("=");
                        double bal = Double.parseDouble(acc[1]);
                        bankmap.put(acc[0], bal);
                    }
                    catch(ArrayIndexOutOfBoundsException aioobe){
                        logger.warning("Error reading line for BankAccount: "+acc[0]);
                        continue;
                    }
                    catch(NumberFormatException nfe){
                        logger.warning("Error reading line for BankAccount: "+acc[0]);
                        continue;
                    }
                }
            }
            catch(IOException ioe){
                logger.log(Level.SEVERE, "Error reading File: dCBanks.txt", ioe);
                logger.severe("dConomy will now be disabled");
                return false;
            }
        }
        return true;
    }
    
    private void loadJointAccounts(){
        synchronized(jointmap){
            String[] accounts = jointdir.list();
            if(accounts != null){
                for(String acc : accounts){
                    try{
                        Properties account = new Properties();
                        account.load(new FileInputStream(new File(jds+acc)));
                        String name = acc;
                        String[] owners = account.getProperty("owners").split(",");
                        String[] users = account.getProperty("users").split(",");
                        double balance = Double.parseDouble(account.getProperty("balance"));
                        double muw = Double.parseDouble(account.getProperty("UserMaxWithdraw"));
                        int delay = DCoProperties.getJointDelay();
                        long reset = DCoProperties.getJointDelay();
                        if(account.containsKey("JointWithdrawDelay")){
                            delay = Integer.parseInt(account.getProperty("JointWithdrawDelay"));
                        }
                        if(account.containsKey("DelayReset")){
                            reset = Long.parseLong(account.getProperty("DelayReset"));
                        }
                        JointAccount joint = new JointAccount(users, owners, balance, muw, delay, reset);
                        jointmap.put(name, joint);
                    }
                    catch (NumberFormatException nfe){
                        logger.warning("Error reading file for JointAccount: "+acc);
                        continue;
                    } 
                    catch (FileNotFoundException fnfe) {
                        logger.warning("Error reading file for JointAccount: "+acc);
                        continue;
                    }
                    catch (IOException ioe) {
                        logger.warning("Error reading file for JointAccount: "+acc);
                        continue;
                    }
                }
            }
        }
    }
    
    public void saveMaps(){
        super.saveMaps();
        FileOutputStream out;
        logger.info("[dConomy] Saving accounts...");
        synchronized(accmap){
            try{
                Properties account = new Properties();
                for(String acc : accmap.keySet()){
                    account.setProperty(acc, String.valueOf(accmap.get(acc)));
                }
                out = new FileOutputStream(accFile);
                account.store(out, null);
                out.close();
            }
            catch (IOException ioe) {
                logger.warning("Error saving to dCAccounts.txt");
            }
        }
        synchronized(bankmap){
            try{
                Properties account = new Properties();
                for(String acc : bankmap.keySet()){
                    account.setProperty(acc, String.valueOf(accmap.get(acc)));
                }
                out = new FileOutputStream(bankFile);
                account.store(out, null);
                out.close();
            }
            catch (IOException ioe) {
                logger.warning("Error saving to dCBanks.txt");
            }
        }
        synchronized(jointmap){
            for(String acc : jointmap.keySet()){
                try{
                    Properties account = new Properties();
                      
                    JointAccount joint = jointmap.get(acc);
                    String owners = joint.getOwners();
                    String users = joint.getUsers();
                    String balance = String.valueOf(joint.getBalance());
                    String muw = String.valueOf(joint.getMaxUserWithdraw());
                    String delay = String.valueOf(joint.getDelay());
                    String reset = String.valueOf(joint.getReset());
                    account.setProperty("owners", owners);
                    account.setProperty("users", users);
                    account.setProperty("balance", balance);
                    account.setProperty("UserMaxWithdraw", muw);
                    account.setProperty("WithdrawDelay", delay);
                    account.setProperty("DelayReset", reset);
                    out = new FileOutputStream(bankFile);
                    account.store(out, null);
                    out.close();
                }
                catch (IOException ioe) {
                    logger.warning("Error saving to Joint Account File: "+acc+".txt");
                }
            }
        }
        logger.info("[dConomy] Saving complete.");
    }
    
    public void logTrans(String action){
        if(DCoProperties.isLogging()){
            date = new Date();
            String filename= String.format(TransLoc, String.valueOf(dateFormat.format(date)));
            File LogFile = new File(filename);
            if (!LogFile.exists()){ 
                try {
                    LogFile.createNewFile();
                } catch (IOException e) {
                    logger.warning("[dConomy] Unable to create new Log File!");
                }
            }
            try {
                FileWriter fw = new FileWriter(filename,true);
                fw.write("["+dateFormatTime.format(date)+"] "+action+ System.getProperty("line.separator"));
                fw.close();  
            } catch (IOException e) {
                logger.warning("[dConomy] Unable to Log Transaction: "+action);
            }
        }
    }
}
