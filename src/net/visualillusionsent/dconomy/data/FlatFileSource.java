package net.visualillusionsent.dconomy.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * FlatFileSource.java FlatFile data handler
 * 
 * @author darkdiplomat
 *          <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 * @version 2.0
 * @see DataSource
 */
public class FlatFileSource extends DataSource{
    private String jds = DCoProperties.getDir()+"JointAccounts/";
    private String TransLoc = DCoProperties.getDir()+/*date(day/month/year)*/"%s.txt";
    private File accFile = new File(DCoProperties.getDir()+"dCAccounts.txt");
    private File bankFile = new File(DCoProperties.getDir()+"dCBanks.txt");
    private File jointdir = new File(jds);
   
    
    FlatFileSource(){ }
    
    void loadMaps() {
        if(accFile.exists()){
            loadAccounts();
        }
        else{
            try {
                accFile.createNewFile();
            } 
            catch (IOException ioe) {
                logger.severe("Failed to create File: dCAccounts.txt");
            }
        }
        if(bankFile.exists()){
            loadBanks();
        }
        else{
            try{
                bankFile.createNewFile();
            } 
            catch(IOException ioe){
                logger.severe("Failed to create File: dCBanks.txt");
            }
        }
        if(jointdir.isDirectory()){
            loadJointAccounts();
        }
        else{
            jointdir.mkdir();
        }
    }
    
    private void loadAccounts(){
        synchronized(accmap){
            try{
                BufferedReader in = new BufferedReader(new FileReader(accFile));
                String line;
                String[] acc = new String[]{ "" };
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
                logger.severe("Error reading File: dCAccounts.txt");
            }
        }
    }
    
    private void loadBanks(){
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
                logger.severe("Error reading File: dCBanks.txt");
            }
        }
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
                        JointAccount joint = new JointAccount(users, owners, balance, muw);
                        jointmap.put(name, joint);
                    }
                    catch (NumberFormatException nfe){
                        
                    } 
                    catch (FileNotFoundException fnfe) {
                        
                    }
                    catch (IOException ioe) {
                        
                    }
                }
            }
        }
    }
    
    public void saveMaps(){
        
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
                    logger.warning("[dConomy] - Unable to create new Log File!");
                }
            }
            try {
                FileWriter fw = new FileWriter(filename,true);
                fw.write("["+dateFormatTime.format(date)+"] "+action+ System.getProperty("line.separator"));
                fw.close();  
            } catch (IOException e) {
                logger.warning("[dConomy] - Unable to Log Transaction!");
            }
        }
    }
}
