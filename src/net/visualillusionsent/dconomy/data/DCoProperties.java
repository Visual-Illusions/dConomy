package net.visualillusionsent.dconomy.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * dCoProperties.java - dConomy Properties handler class
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class DCoProperties {
    static Logger logger = Logger.getLogger("Minecraft");
    private static double accinitial, binterest, jmaxdraw;
    private static int bdelay, jdelay;
    private static long breset, jreset;
    private static String dir = "plugins/config/dConomy/", moneyname;
    private static DataSource ds;
    private static boolean mysql, cmysql, log, aoc, caa, mbj;
    
    private static final File dCoMySQL = new File("plugins/config/dConomy/dCMySQLConn.ini");
    private static final File CMySQLFile = new File("mysql.properties");
    private static final File dCoPropsFile = new File("plugins/config/dConomy/dCSettings.ini");
    private static Properties dCoProps = new Properties();
    
    public static boolean load(){
        File dire = new File(dir);
        if(!dire.exists()){
            dire.mkdirs();
        }
        if(!dCoPropsFile.exists()){
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(dCoPropsFile));
                out.write("###dConomy Settings###"); out.newLine();
                out.write("#New Account Starting Balance value in 0.00 format (if set to less than 0.01 will default to 0)#"); out.newLine();
                out.write("Starting-Balance=0"); out.newLine();
                out.write("#Name to call your currency#"); out.newLine();
                out.write("Money-Name=Voin$"); out.newLine();
                out.write("#Set to true to use MySQL#"); out.newLine();
                out.write("Use-MySQL=false"); out.newLine();
                out.write("#Set to true to use Canary's MySQL Connection (MySQL needs to be true)#"); out.newLine();
                out.write("Use-Canary-MySQLConn=false"); out.newLine();
                out.write("#Set to true to log transactions#"); out.newLine();
                out.write("LogPayments=false"); out.newLine();
                out.write("#Bank Interest Percentage (0.02 = 0.02%) (if set to 0 or less will default to 0.02)#"); out.newLine();
                out.write("Bank-InterestRate=0.02"); out.newLine();
                out.write("#Bank Interest Pay Delay (in minutes) - Set to 0 to disable"); out.newLine();
                out.write("Bank-InterestPayDelay=15"); out.newLine();
                out.write("#Joint Account Users Withdraw delay(in minutes) - Set to 0 to disable#"); out.newLine();
                out.write("JointUserWithdrawDelay=5"); out.newLine();
                out.write("#Joint Account User Max Withdraw Default#"); out.newLine();
                out.write("JointUserMaxWithdrawAmount=50"); out.newLine();
                out.write("#Admin Only Check Another Players Balance - Set to false to allow all#"); out.newLine();
                out.write("AOCAPB=false"); out.newLine();
                out.write("#Allows Accounts to be created always (no checking of /money permissions)"); out.newLine();
                out.write("CreateAccountsAlways=true"); out.newLine();
                out.write("#Set prefix to [Money] [Bank] [Joint] instead of [dCo]"); out.newLine();
                out.write("Prefix-MBJ=false"); out.newLine();
                out.write("###EOF###");
                out.close();
            } 
            catch (IOException e) {
                logger.severe("[dConomy] - Unable to create Properties File... dConomy will now be disabled...");
                return false;
            }
            logger.info("[dConomy] - dCSettings.ini created!"); 
        }
        
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(dCoPropsFile);
            dCoProps.load(stream);
        } 
        catch (IOException ex) {
            logger.warning("[dConomy] - Unable to load Settings... dConomy will now be disabled...");
            return false;
        }
        
        accinitial = parseDouble(0, "Starting-Balance");
        moneyname = parseString("Voin$", "Money-Name");
        mysql = parseBoolean(false, "Use-MySQL");
        cmysql = parseBoolean(false, "Use-Canary-MySQLConn");
        log = parseBoolean(false, "LogPayments");
        binterest = parseDouble(0, "Bank-InterestRate");
        bdelay = parseInt(15, "Bank-InterestPayDelay");
        jdelay = parseInt(5, "JointUserWithdrawDelay");
        jmaxdraw = parseDouble(50, "JointUserMaxWithdrawAmount");
        aoc = parseBoolean(false, "AOCAPB");
        mbj = parseBoolean(false, "Prefix-MBJ");
        caa = parseBoolean(true, "CreateAccountsAlways");
        
        //breset = dCTimerreset.getLong("BankTimerResetTo");
        //jreset = dCTimerreset.getLong("JointWithdrawTimerResetTo");
        
        if (accinitial < 0.01 && accinitial != 0){
            logger.warning("[dConomy] - Starting balance was less than 0.01 and greater than 0. Defaulting to 0.");
            accinitial = 0;
        }
        
        logger.info("[dConomy] - Settings loaded!");
        
        String database=null, username=null, password=null, driver=null;
        Properties dcsql = new Properties();
        
        if(!dCoMySQL.exists()){
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(dCoPropsFile));
                out.write("Driver=com.mysql.jdbc.Driver"); out.newLine();
                out.write("Password=root"); out.newLine();
                out.write("DataBase=jdbc:mysql://localhost:3306/minecraft"); out.newLine();
                out.write("UserName=root"); out.newLine();
                out.close();
            } 
            catch (IOException e) {
                logger.severe("[dConomy] - Unable to create MySQL Properties File");
            }
        }
        
        if(mysql){
            if(!cmysql){
                try {
                    stream = new FileInputStream(dCoMySQL);
                    dcsql.load(stream);
                    database = dcsql.getProperty("DataBase");
                    username = dcsql.getProperty("UserName");
                    password = dcsql.getProperty("Password");
                    driver = dcsql.getProperty("Driver");
                    stream.close();
                } catch (IOException ex) {
                    logger.warning("[dConomy] - Unable to load MySQL Settings...");
                    mysql = false;
                }
                
                if(mysql){
                    try {
                        Class.forName(driver);
                    } 
                    catch (ClassNotFoundException cnfe) {
                        logger.warning("[dConomy] - Unable to find driver class: " + driver);
                        logger.warning("[dConomy] - Disabling SQL!");
                        mysql = false;
                    }
                }
            }
            else{
                try {
                    stream = new FileInputStream(CMySQLFile);
                    dcsql.load(stream);
                    database = dcsql.getProperty("db");
                    username = dcsql.getProperty("user");
                    password = dcsql.getProperty("pass");
                    stream.close();
                } 
                catch (IOException ex) {
                    logger.warning("[dConomy] - Unable to load MySQL Settings...");
                    mysql = false;
                }
            }
        }
        
        if(mysql){
            ds = new MySqlSource(database, username, password);
            logger.info("[dConomy] - MySQL Setting Loaded!");
        }
        else{
            ds = new FlatFileSource();
        }
        return true;
    }
    
    private static Boolean parseBoolean(boolean def, String key){
        boolean value = def;
        if(dCoProps.containsKey(key)){
            try{
                value = Boolean.parseBoolean(dCoProps.getProperty(key));
            }
            catch(Exception e){
                value = def;
                logger.warning("[dConomy] - Invaild Value for "+key+" Using default of "+def);
            }
        }   
        else{
            logger.warning("[dConomy] - Key: '"+key+"' not found. Using default of "+def);
        }
        return value;
    }
    
    private static String parseString(String def, String key){
        String value = def;
        if(dCoProps.containsKey(key)){
            value = dCoProps.getProperty(key);
            if(value.equals("") || value.equals(" ") || value == null){
                value = def;
                logger.warning("[dConomy] - Invaild Value for "+key+" Using default of "+def);
            }
        }
        else{
            logger.warning("[dConomy] - Key: '"+key+"' not found. Using default of "+def);
        }
        return value;
    }
    
    private static Integer parseInt(int def, String key){
        int value = def;
        if(dCoProps.containsKey(key)){
            try{
                value = Integer.parseInt(dCoProps.getProperty(key));
            }catch(NumberFormatException NFE){
                value = def;
                logger.warning("[dConomy] - Value was invaild for "+key+" Using default of "+def);
            }
        }
        else{
            logger.warning("[dConomy] - Key: '"+key+"' not found. Using default of "+def);
        }
        return value;
    }
    
    private static Double parseDouble(double def, String key){
        double value = def;
        if(dCoProps.containsKey(key)){
            try{
                value = Double.parseDouble(dCoProps.getProperty(key));
            }catch(NumberFormatException NFE){
                String checkval  = dCoProps.getProperty(key).replace(",", ".");
                try{
                    value = Double.parseDouble(checkval);
                }catch(NumberFormatException NFE2){
                    value = def;
                    logger.warning("[dConomy] - Value was invaild for "+key+" Using default of "+def);
                }
            }
        }
        else{
            logger.warning("[dConomy] - Key: '"+key+"' not found. Using default of "+def);
        }
        return value;
    }
    
    /**
     * Returns user account's starting balance
     * 
     * @return the starting balance for user account
     */
    public static double getInitialBalance(){
        return accinitial;
    }
    
    public static String getDir(){
        return dir;
    }
    
    public static String getMoneyName(){
        return moneyname;
    }
    
    public static DataSource getDS(){
        return ds;
    }
    
    public static boolean isLogging(){
        return log;
    }
    
    public static boolean getAOC(){
        return aoc;
    }
    
    public static boolean getMBJ(){
        return mbj;
    }
    
    public static boolean getCAA(){
        return caa;
    }
    
    public static double getJointMaxDraw(){
        return jmaxdraw;
    }
    
    public static double getBankInterest(){
        return binterest;
    }
    
    public static int getBankDelay(){
        return bdelay;
    }
}