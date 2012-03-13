import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dCData {
    //Misc Stuff
    public final Logger log = Logger.getLogger("Minecraft");
    private final Server server = etc.getServer();
    
    //PropertiesFiles
    private PropertiesFile dCSettings;
    private PropertiesFile dCMySQLConn;
    private PropertiesFile dCMessage;
    private PropertiesFile dCJointAccounts;
    private PropertiesFile dCPayForwarding;
    private PropertiesFile dCTimerreset;
    
    //Properties File Locations/Directories
    private final String dire = "plugins/config/dConomy/";
    private final String direJoint = "plugins/config/dConomy/JointAccounts/";
    private final String direTrans = "plugins/config/dConomy/TransactionLogs/";
    private final String direMessages = "plugins/config/dConomy/Messages/";
    private final String propsLoc = "dCSettings.ini";
    private final String MySQLLoc = "dCMySQLConn.ini";
    private final String FormatLoc = "dCMessageFormat.txt";
    private final String JointAcc = /*AccountName*/".txt";
    private final String TransLoc = /*date(day/month/year)*/".txt";
    private final String JUWDLoc = "JUWD.txt";
    private final String dCPF = "PayForwarding.txt";
    private final String timerresetloc = "dCTimerReser.DONOTEDIT";
    private final String NL = System.getProperty("line.separator");
    
    //Date stuff for logs
    private Date date;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    
    //Settings
    //private PluginListener dCListener;
    boolean MySQL = false, CMySQL = false, Log = false, MBJ = false, iConvert = false, aoc = false, CAA = false;
    private String DataBase = "jdbc:mysql://localhost:3306/minecraft", UserName = "root", Password= "root", Driver = "com.mysql.jdbc.Driver", MoneyName = "Voin$";
    double startingbalance = 100.00, JUMWA = 25, interest = 0.02;
    private int Bankdelay = 60, JWDelay = 60;
    private long breset = System.currentTimeMillis()+(Bankdelay*60*1000), jwreset = System.currentTimeMillis()+(JWDelay*60*1000);
    protected dCTimer dCT;
    private dCMessages dCM;
    
    //Number Formating
    private NumberFormat displayform = new DecimalFormat("#,##0.00");
    
    //Misc
    private String PFCS = "-PF-ON";
    private String PFAS = "-PF-ACC";
    private ArrayList<String> JUWD;
    
    private Object lock = new Object();
    
    //Data Initializer
    public dCData(){
        createDirectory();
        dCSettings = new PropertiesFile(dire+propsLoc);
        dCMySQLConn = new PropertiesFile(dire+MySQLLoc);
        dCMessage = new PropertiesFile(direMessages+FormatLoc);
        dCPayForwarding = new PropertiesFile(dire+dCPF);
        dCTimerreset = new PropertiesFile(dire+timerresetloc);
        loadSettings();
    }

    //Make the Directory if not exist and Add some order to the Messages File
    private void createDirectory(){
        boolean firstRun = false;
        File checkDir = new File(dire);
        File checkDireJoint = new File(direJoint);
        File checkDirLog = new File(direTrans);
        File checkDirMess = new File(direMessages);
        File PropsFile = new File(dire+propsLoc);
        File MessFile = new File(direMessages+FormatLoc);
        if (!checkDir.exists()){
            log.info("[dConomy] - First Run Detected! Creating Directories and Files!");
            firstRun = true;
            checkDir.mkdirs();
            log.info("[dConomy] - Main Directory created!");
        }
        if (!checkDireJoint.exists()){
            if(!firstRun){ log.info("[dConomy] - JointAccounts Folder was missing! Recreating!"); }
            checkDireJoint.mkdirs();
            log.info("[dConomy] - JointAccounts Folder created!");
        }
        if (!checkDirLog.exists()){
            if(!firstRun){ log.info("[dConomy] - TransactionLogs Folder was missing! Recreating!"); }
            checkDirLog.mkdirs();
            log.info("[dConomy] - TransactionLogs Folder created!");
        }
        if (!checkDirMess.exists()){
            if(!firstRun){ log.info("[dConomy] - Messages Folder was missing! Recreating!"); }
            checkDirMess.mkdirs();
            log.info("[dConomy] - Messages Folder created!");
        }
        if(!PropsFile.exists()){
            if(!firstRun){ log.info("[dConomy] - dCSettings.ini was missing! Recreating!"); }
            CreatePropsFile(false);
        }
        if (!MessFile.exists()){
            if(!firstRun){ log.info("[dConomy] - dCMessageFormat.txt was missing! Recreating!"); }
            boolean fail = false;
            //Keeping the Messages File from becoming a mess
            try{
                InputStream in = getClass().getClassLoader().getResourceAsStream("dCMessageFormat.txt");
                FileWriter out = new FileWriter(MessFile);
                int c;
                while ((c = in.read()) != -1){
                    out.write(c);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                log.severe("[dConomy] - Unable to create Messages File");
                fail = true;
            }
            if(!fail){ log.info("[dConomy] - dCMessageFormat.txt created!"); }
        }
    }
    //With iConvert needing to be switched off  need to make sure the Props File stays clean So we will recreate the file
    //Also this will be used with first run
    private void CreatePropsFile(boolean iConvertCalled){
        File PropsFile = new File(dire+propsLoc);
        boolean fail = false;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(PropsFile));
            out.write("###dConomy Settings###"); out.newLine();
            out.write("#New Account Starting Balance value in 0.00 format (if set to less than 0.01 will default to 0)#"); out.newLine();
            out.write("Starting-Balance="+startingbalance); out.newLine();
            out.write("#Name to call your currency#"); out.newLine();
            out.write("Money-Name="+MoneyName); out.newLine();
            out.write("#Set to true to use MySQL#"); out.newLine();
            out.write("Use-MySQL="+MySQL); out.newLine();
            out.write("#Set to true to use Canary's MySQL Connection (MySQL needs to be true)#"); out.newLine();
            out.write("Use-Canary-MySQLConn="+CMySQL); out.newLine();
            out.write("#Set to true to log transactions#"); out.newLine();
            out.write("LogPayments="+Log); out.newLine();
            out.write("#Bank Interest Percentage (0.02 = 0.02%) (if set to 0 or less will default to 0.02)#"); out.newLine();
            out.write("Bank-InterestRate="+interest); out.newLine();
            out.write("#Bank Interest Pay Delay (in minutes) - Set to 0 to disable"); out.newLine();
            out.write("Bank-InterestPayDelay="+Bankdelay); out.newLine();
            out.write("#Joint Account Users Withdraw delay(in minutes) - Set to 0 to disable#"); out.newLine();
            out.write("JointUserWithdrawDelay="+JWDelay); out.newLine();
            out.write("#Joint Account User Max Withdraw Default#"); out.newLine();
            out.write("JointUserMaxWithdrawAmount="+JUMWA); out.newLine();
            out.write("#Admin Only Check Another Players Balance - Set to false to allow all#"); out.newLine();
            out.write("AOCAPB="+aoc); out.newLine();
            out.write("#Allows Accounts to be created always (no checking of /money permissions)"); out.newLine();
            out.write("CreateAccountsAlways="+CAA); out.newLine();
            out.write("#Set to true to convert iCo Balances to dCo Balances (reverts to false after convert runs)#"); out.newLine();
            out.write("Convert-iConomy="+iConvert); out.newLine();
            out.write("###EOF###");
            out.close();
        } catch (IOException e) {
            log.severe("[dConomy] - Unable to create Properties File");
            fail = true;
        }
        if(!(fail && iConvertCalled)){ log.info("[dConomy] - dCSettings.ini created!"); }
    }
    
    //Load settings
    private void loadSettings(){
        //Get/Set Settings
        startingbalance = parseDouble(startingbalance, "Starting-Balance");
        MoneyName = parseString(MoneyName, "Money-Name");
        MySQL = parseBoolean(MySQL, "Use-MySQL");
        CMySQL = parseBoolean(CMySQL, "Use-Canary-MySQLConn");
        Log = parseBoolean(Log, "LogPayments");
        interest = parseDouble(interest, "Bank-InterestRate");
        Bankdelay = parseInt(Bankdelay, "Bank-InterestPayDelay");
        JWDelay = parseInt(JWDelay, "JointUserWithdrawDelay");
        breset = dCTimerreset.getLong("BankTimerResetTo");
        jwreset = dCTimerreset.getLong("JointWithdrawTimerResetTo");
        iConvert = parseBoolean(iConvert, "Convert-iConomy");
        JUMWA = parseDouble(JUMWA, "JointUserMaxWithdrawAmount");
        aoc = parseBoolean(aoc, "AOCAPB");
        MBJ = parseBoolean(MBJ, "Prefix-MBJ");
        CAA = parseBoolean(CAA, "CreateAccountsAlways");
        
        if (startingbalance < 0.01){
            log.warning("[dConomy] - Starting balance was less than 0.01 and greater than 0. Defaulting to 0.");
            startingbalance = 0;
        }
        
        log.info("[dConomy] - Settings loaded!");
        
        DataBase = dCMySQLConn.getString("DataBase", DataBase);
        UserName = dCMySQLConn.getString("UserName", UserName);
        Password = dCMySQLConn.getString("Password", Password);
        Driver = dCMySQLConn.getString("Driver", Driver);
        
        if((!CMySQL) && (MySQL)){
            try {
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                log.warning("[dConomy] - Unable to find driver class: " + Driver);
                log.warning("[dConomy] - Disabling SQL!");
                MySQL = false;
            }
        }
        
        if(MySQL){
            log.info("[dConomy] - MySQL Setting Loaded!");
        }
        
        JUWD = new ArrayList<String>();
        dCT = new dCTimer(this);
        //Start Bank Interest Timer if Enabled
        if (interest > 0){
            interest = interest/100; //pull it down to correct %
        }else if (interest < 0){
            interest = 0.02/100;
        }
        if (Bankdelay > 0){
            breset = breset - System.currentTimeMillis();
            if (breset < 0){
                breset = 1000;
            }
            dCT.SetUpBT(Bankdelay, interest, breset);
            log.info("[dConomy] - Bank Interest Timer Started!");
        }
        //Start JointAccount User Withdraw Delay if Enabled
        if (JWDelay > 0){
            jwreset = jwreset - System.currentTimeMillis();
            if (jwreset < 0){
                jwreset = 1000;
            }
            dCT.SetUpJWDT(JWDelay, jwreset);
            log.info("[dConomy] - JointAccount User Withdraw Delay Timer Started!");
        }
        
        dCM = new dCMessages(this, dCMessage);
        dCM.loadMessages();
        
        if (MySQL){
            CreateTable();
        }
        
        if (iConvert){
            iConvertion();
        }
        
        if (JWDelay > 0){
            String filename= direJoint + JUWDLoc;
            File file = new File(filename);
            if(file.exists()){
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filename));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        JUWD.add(line);
                    }
                    reader.close();
                }catch (IOException e){
                    log.severe("[dConomy] - Unable to Load JUWD Users");
                }
            }
        }
    }
    
    private Boolean parseBoolean(boolean def, String key){
        boolean value = def;
        if(dCSettings.containsKey(key)){
            try{
                value = dCSettings.getBoolean(key);
            }catch(Exception e){
                value = def;
                log.warning("[dConomy] - Invaild Value for "+key+NL+" Using default of "+def);
            }
        }
        else{
            log.warning("[dConomy] - Key: '"+key+"' not found."+NL+" Using default of "+def);
        }
        return value;
    }
    
    private String parseString(String def, String key){
        String value = def;
        if(dCSettings.containsKey(key)){
            value = dCSettings.getString(key);
            if(value.equals("") || value.equals(" ")){
                value = def;
                log.warning("[dConomy] - Invaild Value for "+key+NL+" Using default of "+def);
            }
        }
        else{
            log.warning("[dConomy] - Key: '"+key+"' not found."+NL+" Using default of "+def);
        }
        return value;
    }
    
    private Integer parseInt(int def, String key){
        int value = def;
        if(dCSettings.containsKey(key)){
            try{
                value = dCSettings.getInt(key);
            }catch(NumberFormatException NFE){
                value = def;
                log.warning("[dConomy] - Value was invaild for "+key+NL+" Using default of "+def);
            }
        }
        else{
            log.warning("[dConomy] - Key: '"+key+"' not found."+NL+" Using default of "+def);
        }
        return value;
    }
    
    private Double parseDouble(double def, String key){
        double value = def;
        if(dCSettings.containsKey(key)){
            try{
                value = dCSettings.getDouble(key);
            }catch(NumberFormatException NFE){
                String checkval  = dCSettings.getString(key).replace(",", ".");
                try{
                    value = Double.parseDouble(checkval);
                }catch(NumberFormatException NFE2){
                    value = def;
                    log.warning("[dConomy] - Value was invaild for "+key+NL+" Using default of "+def);
                }
            }
        }
        else{
            log.warning("[dConomy] - Key: '"+key+"' not found."+NL+" Using default of "+def);
        }
        return value;
    }
    
    public void SetReset(String type, long time){
        jwreset = time;
        dCTimerreset.setLong(type, time);
    }
    
    public boolean JointUserWithDrawDelayCheck(String name){
        return JUWD.contains(name);
    }
    
    public void JointUserWithdrawDelayAdd(String name){
        if (JWDelay > 0){
            JUWD.add(name);
            String filename= direJoint + JUWDLoc;
            File JUWDFile = new File(filename);
            if (!JUWDFile.exists()){ 
                try {
                    JUWDFile.createNewFile();
                } catch (IOException e) {
                    log.severe("[dConomy] - Unable to create new JUWD File!");
                } 
            }
            try {
                FileWriter fw = new FileWriter(filename,true);
                fw.write(name+System.getProperty("line.separator"));
                fw.close();  
            } catch (IOException e) {
                log.severe("[dConomy] - Unable to add user to JUWD Transaction!");
            }
        }
    }
    
    public void JointUserWithdrawDelayReset(){
        JUWD.clear();
        String filename= direJoint + JUWDLoc;
        File JUWDFile = new File(filename);
        try {
            JUWDFile.createNewFile();
        } catch (IOException e) {
            log.severe("[dConomy] - Unable to create new JUWD File!");
        }
    }
    
    //Return Starting Balance for new players
    public double getStartingBalance(){
        return startingbalance;
    }
    
    public boolean JointAccountUserCheck(String player, String name){
        String users = getJointUsers(name);
        String[] usersplit = users.split(",");
        for (int i = 0; i < usersplit.length; i++){
            if (usersplit[i].equals(player)){
                return true;
            }
        }
        return false;
    }
    
    public boolean JointAccountOwnerCheck(String player, String name){
        String owners = getJointOwners(name);
        String[] ownersplit = owners.split(",");
        for (int i = 0; i < ownersplit.length; i++){
            if (ownersplit[i].equals(player)){
                return true;
            }
        }
        return false;
    }
    
    //Return MoneyName
    public String getMoneyName(){
        return MoneyName;
    }
    
    public Connection getSQLConn() throws SQLException{
        Connection conn = null;
        if (CMySQL){
            conn = etc.getSQLConnection();
        }else{
            conn = DriverManager.getConnection(DataBase, UserName, Password);
        }
        return conn;
    }
    
    private void CreateTable(){
        String table1 = ("CREATE TABLE IF NOT EXISTS `dConomy` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Player` varchar(16) NOT NULL, `Account` DECIMAL(64,2) NOT NULL, `Bank` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
        String table2 = ("CREATE TABLE IF NOT EXISTS `dConomyJoint` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Name` varchar(32) NOT NULL, `Owners` text NOT NULL, `Users` text NOT NULL, `Balance` DECIMAL(64,2) NOT NULL, `UserMaxWithdraw` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
        String table3 = ("CREATE TABLE IF NOT EXISTS `dConomyLog` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Date` varchar(32) NOT NULL, `Time` varchar(32) NOT NULL, `Transaction` Text NOT NULL, PRIMARY KEY (`ID`))");
        Connection conn = null;
        Statement st = null;
        try{
            conn = getSQLConn();
        }catch(SQLException SQLE){
            SQLConnError(SQLE);
        }
        if(conn != null && !dConomy.Terminated){
            try{
                st = conn.createStatement();
                st.addBatch(table1);
                st.addBatch(table2);
                st.addBatch(table3);
                st.executeBatch();
            }catch (SQLException SQLE) {
                SQLError(SQLE, "dCData.CreateTable()");
            }finally{
                try{
                    if(st != null && !st.isClosed()){ st.close(); }
                    if(conn != null && !conn.isClosed()){ conn.close(); }
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
            }
        }
    }
    
    public boolean keyExists(String key, String type){
        boolean exists = false;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null && !dConomy.Terminated){
                    try{
                        ps = conn.prepareStatement("SELECT * FROM dConomy WHERE Player = ?");
                        ps.setString(1, key);
                        rs = ps.executeQuery();
                        if (rs.next()){ exists = true; }
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.keyExists() ", ex);
                    }finally{
                        try{
                            if(rs != null && !rs.isClosed()){ rs.close(); }
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                String file = "";
                try{
                    file = getFile(type);
                    PropertiesFile check = new PropertiesFile(dire+file);
                    exists = check.containsKey(key);
                }catch(Exception E){
                    FileLoadError(E, file);
                }
            }
        }
        return exists;
    }
    
    public boolean JointkeyExists(String name){
        boolean exists = false;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("SELECT * FROM dConomyJoint WHERE Name = ?");
                        ps.setString(1, name);
                        rs = ps.executeQuery();
                        exists = rs.next();
                        conn.close();
                    } catch (SQLException SQLE) {
                        SQLError(SQLE, "dCData.JointKeyExists");
                    }finally{
                        try{
                            if(rs != null){ rs.close(); }
                            if(ps != null){ ps.close(); }
                            if(conn != null){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                String jointloc = getJointFile(name);
                try{
                    File joint = new File(jointloc);
                    exists = joint.exists();
                }catch(Exception E){
                    FileLoadError(E, jointloc);
                }
            }
        }
        return exists;
    }

    public double getBalance(String player, String type){
        synchronized(lock){
            if (MySQL){
                String column = getMySQLColumn(type);
                double bal = 0;
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                try{
                    ps = conn.prepareStatement("SELECT "+column+" FROM dConomy WHERE Player = ?");
                    ps.setString(1, player);
                    rs = ps.executeQuery();
                    if (rs.next()){
                        bal = Double.parseDouble(rs.getString(column));
                    }
                }catch (SQLException ex){
                    SQLError(ex, "dCData.getBalance");
                }finally{
                    try{
                        if(rs != null){ rs.close(); }
                        if(ps != null){ ps.close(); }
                        if(conn != null){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
                return bal;
            }else{
                String file = getFile(type);
                try{
                    PropertiesFile acc = new PropertiesFile(dire+file);
                    return acc.getDouble(player);
                }catch(Exception E){
                    FileLoadError(E, file);
                }
            }
        }
        return (Double) null;
    }
    
    public void setBalance(double bal, String player, String type){
        double newbal = bal;
        synchronized(lock){
            if (MySQL){
                String column = getMySQLColumn(type);
                Connection conn = null;
                PreparedStatement ps = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("UPDATE dConomy SET "+column+" = ? WHERE Player = ? LIMIT 1");
                        ps.setDouble(1, newbal);
                        ps.setString(2, player);
                        ps.executeUpdate();
                    }catch (SQLException SQLE) {
                        SQLError(SQLE, "dCData.setBalance");
                    }finally{
                        try{
                            if(ps != null){ ps.close(); }
                            if(conn != null){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                String file = getFile(type);
                try{
                    PropertiesFile acc = new PropertiesFile(dire+file);
                    acc.setDouble(player, newbal);
                }catch(Exception E){
                    FileSaveError(E, file);
                }
            }
        }
    }
    
    public void setInitialBalance(double bal, String player){
        double newbal = bal;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("INSERT INTO dConomy (Player, Account, Bank) VALUES(?,?,?)");
                        ps.setString(1, player);
                        ps.setDouble(2, newbal);
                        ps.setDouble(3, 0);
                        ps.executeUpdate();
                    } catch (SQLException SQLE) {
                        SQLError(SQLE, "dCData.setInitialBalance");
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    PropertiesFile acc = new PropertiesFile(dire+"dCAccounts.txt");
                    acc.setDouble(player, newbal);
                }catch(Exception E){
                    FileSaveError(E, "dCAccounts.txt");
                }
                try{
                    PropertiesFile bank = new PropertiesFile(dire+"dCBank.txt");
                    bank.setDouble(player, 0);
                }catch(Exception E){
                    FileSaveError(E, "dCBank.txt");
                }
            }
        }
    }
    
    public String getJointUsers(String name){
        String Users = "";
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("SELECT Users FROM dConomyJoint WHERE Name = ?");
                        ps.setString(1, name);
                        rs = ps.executeQuery();
                        if (rs.next()){
                            Users = rs.getString("Users");
                        }
                    }catch (SQLException SQLE){
                        SQLError(SQLE, "dCData.getJointUsers");
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                    Users = dCJointAccounts.getString("Users");
                }catch(Exception E){
                    FileLoadError(E, (name + JointAcc));
                }
            }
        }
        return Users;
    }
    
    public String getJointOwners(String name){
        String Owners = "";
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("SELECT Owners FROM dConomyJoint WHERE Name = ?");
                        ps.setString(1, name);
                        rs = ps.executeQuery();
                        if (rs.next()){
                            Owners = rs.getString("Owners");
                        }
                    }catch (SQLException SQLE){
                        SQLError(SQLE, "dCData.getJointOwners");
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                    Owners = dCJointAccounts.getString("Owners");
                }catch(Exception E){
                    FileLoadError(E, (name + JointAcc));
                }
            }
        }
        return Owners;
    }
    
    public double getJointBalance(String name){
        double bal = 0;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("SELECT Balance FROM dConomyJoint WHERE Name = ?");
                        ps.setString(1, name);
                        rs = ps.executeQuery();
                        if (rs.next()){
                            bal = Double.parseDouble(rs.getString("Balance"));
                        }
                    }catch (SQLException SQLE){
                        SQLError(SQLE, "dCData.getJointBalance");
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                    bal = dCJointAccounts.getDouble("balance");
                }catch(Exception E){
                    FileLoadError(E, (name + JointAcc));
                }
            }
        }
        return bal;
    }
    
    public double getJointUserWithdrawMax(String name){
        double bal = 0;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("SELECT UserMaxWithdraw FROM dConomyJoint WHERE Name = ?");
                        ps.setString(1, name);
                        rs = ps.executeQuery();
                        if(rs.next()){
                            bal = Double.parseDouble(rs.getString("UserMaxWithdraw"));
                        }
                    }catch (SQLException ex){
                        log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.getJointUserWithdrawMax() ", ex);
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                    bal = dCJointAccounts.getDouble("UserMaxWithdraw");
                }catch(Exception E){
                    FileLoadError(E, (name + JointAcc));
                }
            }
        }
        return bal;
    }
    
    public void setJointBalance(double bal, String accName){
        double newbal = bal;
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("UPDATE dConomyJoint SET Balance = ? WHERE Name = ? LIMIT 1");
                        ps.setDouble(1, newbal);
                        ps.setString(2, accName);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.setJointlBalance() ", ex);
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + accName + JointAcc);
                    dCJointAccounts.setDouble("balance", newbal);
                }catch(Exception E){
                    FileSaveError(E, (accName + JointAcc));
                }
            }
        }
    }
    
    public void createJointAccount(String name, String owner){
        synchronized(lock){
            if (MySQL){
                Connection conn = null;
                PreparedStatement ps = null;
                try{
                    conn = getSQLConn();
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
                if(conn != null){
                    try{
                        ps = conn.prepareStatement("INSERT INTO dConomyJoint (Name, Owners, Users, Balance, UserMaxWithdraw)VALUES (?,?,?,?,?)", 1);
                        ps.setString(1, name);
                        ps.setString(2, owner);
                        ps.setString(3, ",");
                        ps.setDouble(4, 0);
                        ps.setDouble(5, JUMWA);
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.createJointAccount() ", ex);
                    }finally{
                        try{
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                }
            }else{
                try{
                    dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                    dCJointAccounts.setString("Owners", owner);
                    dCJointAccounts.setString("Users", "");
                    dCJointAccounts.setDouble("balance", 0);
                    dCJointAccounts.setDouble("UserMaxWithdraw", 25);
                }catch(Exception E){
                    FileSaveError(E, (name + JointAcc));
                }
            }
        }
    }
    
    public void deleteJointAccount(String accName){
        if (MySQL){
            Connection conn = null;
            PreparedStatement ps = null;
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                SQLConnError(SQLE);
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("DELETE FROM dConomyJoint WHERE Name = ?");
                    ps.setString(1, accName);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.deleteJointAccount() ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
            }
        }else{
            try{
                File file = new File(direJoint + accName + JointAcc);
                file.delete();
            }catch(Exception E){
                FileSaveError(E, (accName + JointAcc));
            }
        }
    }
    
    public void updateJointOwners(String name, String newOwners){
        if (MySQL){
            Connection conn = null;
            PreparedStatement ps = null;
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                SQLConnError(SQLE);
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("UPDATE dConomyJoint SET Owners = ? WHERE Name = ? LIMIT 1");
                    ps.setString(1, newOwners);
                    ps.setString(2, name);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.updateJointOwners() ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
            }
        }else{
            try{
                dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                dCJointAccounts.setString("Owners", newOwners);
            }catch(Exception E){
                FileSaveError(E, (name + JointAcc));
            }
        }
    }
    
    public void updateJointUsers(String name, String newUsers){
        if (MySQL){
            Connection conn = null;
            PreparedStatement ps = null;
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                SQLConnError(SQLE);
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("UPDATE dConomyJoint SET Users = ? WHERE Name = ? LIMIT 1");
                    ps.setString(1, newUsers);
                    ps.setString(2, name);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.updateJointUsers() ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
            }
        }else{
            try{
                dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                dCJointAccounts.setString("Users", newUsers);
            }catch(Exception E){
                FileSaveError(E, (name + JointAcc));
            }
        }
    }
    
    public void updateJointMaxUserWithdraw(String name, double maxamount){
        if (MySQL){
            Connection conn = null;
            PreparedStatement ps = null;
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                SQLConnError(SQLE);
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("UPDATE dConomyJoint SET UserMaxWithdraw = ? WHERE Name = ? LIMIT 1");
                    ps.setDouble(1, maxamount);
                    ps.setString(2, name);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.updateJointMaxUserWithdraw() ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
            }
        }else{
            try{
                dCJointAccounts = new PropertiesFile(direJoint + name + JointAcc);
                dCJointAccounts.setDouble("MaxUserWithdraw", maxamount);
            }catch(Exception E){
                FileSaveError(E, (name + JointAcc));
            }
        }
    }
    
    public String getFile(String type){
        String File = "";
        if (type.equalsIgnoreCase("Account")){
            File = "dCAccounts.txt";
        }else if (type.equalsIgnoreCase("Bank")){
            File = "dCBank.txt";
        }
        return File;
    }
    
    public String getJointFile(String name){
        String filename = direJoint + name + JointAcc;
        return filename;
    }
    
    public String getMySQLColumn(String type){
        String Column = "";
        if (type.equalsIgnoreCase("account")){
            Column = "Account";
        }else if (type.equalsIgnoreCase("bank")){
            Column = "Bank";
        }
        return Column;
    }
    
    public boolean getPayForwardCheck(String name){
        if(dCPayForwarding.keyExists(name+PFCS)){
            return dCPayForwarding.getBoolean(name+PFCS);
        }
        return false;
    }
    
    public String getPayForwardAccount(String name){
        if (dCPayForwarding.keyExists(name+PFAS)){
            return dCPayForwarding.getString(name+PFAS);
        }
        return "Account";
    }
    
    public void setPayForward(String name, boolean set){
        dCPayForwarding.setBoolean(name+PFCS, set);
    }
    
    public void setPayForwardAcc(String name, String acc){
        dCPayForwarding.setString(name+PFAS, acc);
    }
    
    public String getMessage(int code, String player, String type, double amount){
        switch (code){
        case 201: return parseMessage(dCM.P201, player, type, amount);
        case 202: return parseMessage(dCM.P202, player, type, amount);
        case 203: return parseMessage(dCM.P203, player, type, amount);
        case 204: return parseMessage(dCM.P204, player, type, amount);
        case 205: return parseMessage(dCM.P205, player, type, amount);
        case 206: return parseMessage(dCM.P206, player, type, amount);
        case 207: return parseMessage(dCM.P207, player, type, amount);
        case 208: return parseMessage(dCM.P208, player, type, amount);
        case 209: return parseMessage(dCM.P209, player, type, amount);
        case 210: return parseMessage(dCM.P210, player, type, amount);
        case 211: return parseMessage(dCM.P211, player, type, amount);
        case 212: return parseMessage(dCM.P212, player, type, amount);
        case 213: return parseMessage(dCM.P213, player, type, amount);
        case 214: return parseMessage(dCM.P214, player, type, amount);
        case 215: return parseMessage(dCM.P215, player, type, amount);
        case 216: return parseMessage(dCM.P216, player, type, amount);
        case 217: return parseMessage(dCM.P217, player, type, amount);
        case 218: return parseMessage(dCM.P218, player, type, amount);
        case 219: return parseMessage(dCM.P219, player, type, amount);
        case 220: return parseMessage(dCM.P220, player, type, amount);
        case 221: return parseMessage(dCM.P221, player, type, amount);
        case 222: return parseMessage(dCM.P222, player, type, amount);
        case 223: return parseMessage(dCM.P223, player, type, amount);
        
        case 301: return parseMessage(dCM.J301, player, type, amount);
        case 302: return parseMessage(dCM.J302, player, type, amount);
        case 303: return parseMessage(dCM.J303, player, type, amount);
        case 304: return parseMessage(dCM.J304, player, type, amount);
        case 305: return parseMessage(dCM.J305, player, type, amount);
        case 306: return parseMessage(dCM.J306, player, type, amount);
        case 307: return parseMessage(dCM.J307, player, type, amount);
        case 308: return parseMessage(dCM.J308, player, type, amount);
        case 309: return parseMessage(dCM.J309, player, type, amount);
        case 310: return parseMessage(dCM.J310, player, type, amount);
        case 311: return parseMessage(dCM.J311, player, type, amount);
        case 312: return parseMessage(dCM.J312, player, type, amount);
        
        case 401: return parseMessage(dCM.A401, player, type, amount).replace("§2[§fdCo§2]§f ", "").replace("§2[§fMONEY§2]§f ", " ");
        case 402: return parseMessage(dCM.A402, player, type, amount).replace("§2[§fdCo§2]§f ", "").replace("§2[§fMONEY§2]§f ", " ");
        case 403: return parseMessage(dCM.A403, player, type, amount).replace("§2[§fdCo§2]§f ", "").replace("§2[§fMONEY§2]§f ", " ");
        case 404: return parseMessage(dCM.A404, player, type, amount);
        case 405: return parseMessage(dCM.A405, player, type, amount);
        case 406: return parseMessage(dCM.A406, player, type, amount);
        case 407: return parseMessage(dCM.A407, player, type, amount);
        case 408: return parseMessage(dCM.A408, player, type, amount);
        case 409: return parseMessage(dCM.A409, player, type, amount);
        case 410: return parseMessage(dCM.A410, player, type, amount);
        case 411: return parseMessage(dCM.A411, player, type, amount);
        case 412: return parseMessage(dCM.A412, player, type, amount);
        case 413: return parseMessage(dCM.A413, player, type, amount);
        case 414: return parseMessage(dCM.A414, player, type, amount);
        case 415: return parseMessage(dCM.A415, player, type, amount);
            
        default: return "MESSAGE LINE MISSING MC:"+code;
        }
    }
    
    public String getHelpMessage(int code){
        switch(code){
        case 501: return ColorCode(dCM.H501);
        case 502: return ColorCode(dCM.H502);
        case 503: return ColorCode(dCM.H503);
        case 504: return ColorCode(dCM.H504);
        case 505: return ColorCode(dCM.H505);
        case 506: return ColorCode(dCM.H506);
        case 507: return ColorCode(dCM.H507);
        case 508: return ColorCode(dCM.H508);
        case 509: return ColorCode(dCM.H509);
        case 510: return ColorCode(dCM.H510);
        case 511: return ColorCode(dCM.H511);
        case 512: return ColorCode(dCM.H512);
        case 513: return ColorCode(dCM.H513);
        case 514: return ColorCode(dCM.H514);
        case 515: return ColorCode(dCM.H515);
        case 516: return ColorCode(dCM.H516);
        case 517: return ColorCode(dCM.H517);
        case 518: return ColorCode(dCM.H518);
        case 519: return ColorCode(dCM.H519);
        case 520: return ColorCode(dCM.H520);
        case 521: return ColorCode(dCM.H521);
        case 522: return ColorCode(dCM.H522);
        case 523: return ColorCode(dCM.H523);
        case 524: return ColorCode(dCM.H524);
        case 525: return ColorCode(dCM.H525);
        case 526: return ColorCode(dCM.H526);
        case 527: return ColorCode(dCM.H527);
        case 528: return ColorCode(dCM.H528);
        case 529: return ColorCode(dCM.H529);
        case 530: return ColorCode(dCM.H530);
        case 531: return ColorCode(dCM.H531);
        case 532: return ColorCode(dCM.H532);
        case 533: return ColorCode(dCM.H533);
        case 534: return ColorCode(dCM.H534);
        case 535: return ColorCode(dCM.H535);
        case 536: return ColorCode(dCM.H536);
        case 537: return ColorCode(dCM.H537);
        case 538: return ColorCode(dCM.H538);
        case 539: return ColorCode(dCM.H539);
        case 540: return ColorCode(dCM.H540);
        case 541: return ColorCode(dCM.H541);
        case 542: return ColorCode(dCM.H542);
        case 543: return ColorCode(dCM.H543);
        case 544: return ColorCode(dCM.H544);
        case 545: return ColorCode(dCM.H545);
        case 546: return ColorCode(dCM.H546);
        default: return "HELP LINE MISSING HM:"+code;
        }
    }

    public String getErrorMessage(int code, String name){
        String type = "UNKNOWN";
        switch (code){
        case 101: return parseMessage(dCM.E101, name, type, 0);
        case 102: return parseMessage(dCM.E102, name, type, 0);
        case 103: return parseMessage(dCM.E103, name, type, 0);
        case 104: return parseMessage(dCM.E104, name, type, 0);
        case 105: return parseMessage(dCM.E105, type, name, 0);
        case 106: return parseMessage(dCM.E106, name, type, 0);
        case 107: return parseMessage(dCM.E107, name, type, 0);
        case 108: return parseMessage(dCM.E108, name, type, 0);
        case 109: return parseMessage(dCM.E109, name, type, 0);
        case 110: return parseMessage(dCM.E110, type, name, 0);
        case 111: return parseMessage(dCM.E111, type, name, 0);
        case 112: return parseMessage(dCM.E112, name, type, 0);
        case 113: return parseMessage(dCM.E113, name, type, 0);
        case 114: return parseMessage(dCM.E114, name, type, 0);
        case 115: return parseMessage(dCM.E115, name, type, 0);
        case 116: return parseMessage(dCM.E116, name, type, 0);
        case 117: return parseMessage(dCM.E117, name, type, 0);
        case 118: return parseMessage(dCM.E118, name, type, 0);
        case 119: return parseMessage(dCM.E119, name, type, 0);
        case 120: return parseMessage(dCM.E120, name, type, 0);
        case 121: return parseMessage(dCM.E121, name, type, 0);
        case 122: return parseMessage(dCM.E122, name, type, 0);
        case 123: return parseMessage(dCM.E123, name, type, 0);
        case 124: return parseMessage(dCM.E124, name, type, 0);
        case 125: return parseMessage(dCM.E125, name, type, 0);
        case 126: return parseMessage(dCM.E126, name, type, 0);
        case 127: return parseMessage(dCM.E127, name, type, 0);
        default: return "ERROR CODE MISSING EC:"+code;
        }
    }
    
    public void Logging(int code, String p1, String p2, String amount, String account){
        if (Log){
            switch(code){
            case 601: LogTrans(parseLog(dCM.L601, p1, p2, amount, account)+" (LC:601)"); return;
            case 602: LogTrans(parseLog(dCM.L602, p1, p2, amount, account)+" (LC:602)"); return;
            case 603: LogTrans(parseLog(dCM.L603, p1, p2, amount, account)+" (LC:603)"); return;
            case 604: LogTrans(parseLog(dCM.L604, p1, p2, amount, account)+" (LC:604)"); return;
            case 605: LogTrans(parseLog(dCM.L605, p1, p2, amount, account)+" (LC:605)"); return;
            case 606: LogTrans(parseLog(dCM.L606, p1, p2, amount, account)+" (LC:606)"); return;
            case 607: LogTrans(parseLog(dCM.L607, p1, p2, amount, account)+" (LC:607)"); return;
            case 608: LogTrans(parseLog(dCM.L608, p1, p2, amount, account)+" (LC:608)"); return;
            case 609: LogTrans(parseLog(dCM.L609, p1, p2, amount, account)+" (LC:609)"); return;
            case 610: LogTrans(parseLog(dCM.L610, p1, p2, amount, account)+" (LC:610)"); return;
            case 611: LogTrans(parseLog(dCM.L611, p1, p2, amount, account)+" (LC:611)"); return;
            case 612: LogTrans(parseLog(dCM.L612, p1, p2, amount, account)+" (LC:612)"); return;
            case 613: LogTrans(parseLog(dCM.L613, p1, p2, amount, account)+" (LC:613)"); return;
            case 614: LogTrans(parseLog(dCM.L614, p1, p2, amount, account)+" (LC:614)"); return;
            case 615: LogTrans(parseLog(dCM.L615, p1, p2, amount, account)+" (LC:615)"); return;
            case 616: LogTrans(parseLog(dCM.L616, p1, p2, amount, account)+" (LC:616)"); return;
            case 617: LogTrans(parseLog(dCM.L617, p1, p2, amount, account)+" (LC:617)"); return;
            case 618: LogTrans(parseLog(dCM.L618, p1, p2, amount, account)+" (LC:618)"); return;
            case 619: LogTrans(parseLog(dCM.L619, p1, p2, amount, account)+" (LC:619)"); return;
            case 620: LogTrans(parseLog(dCM.L620, p1, p2, amount, account)+" (LC:620)"); return;
            case 621: LogTrans(parseLog(dCM.L621, p1, p2, amount, account)+" (LC:621)"); return;
            case 622: LogTrans(parseLog(dCM.L622, p1, p2, amount, account)+" (LC:622)"); return;
            case 623: LogTrans(parseLog(dCM.L623, p1, p2, amount, account)+" (LC:623)"); return;
            case 624: LogTrans(parseLog(dCM.L624, p1, p2, amount, account)+" (LC:624)"); return;
            case 625: LogTrans(parseLog(dCM.L625, p1, p2, amount, account)+" (LC:625)"); return;
            case 626: LogTrans(parseLog(dCM.L626, p1, p2, amount, account)+" (LC:626)"); return;
            case 627: LogTrans(parseLog(dCM.L627, p1, p2, amount, account)+" (LC:627)"); return;
            case 628: LogTrans(parseLog(dCM.L628, p1, p2, amount, account)+" (LC:628)"); return;
            case 629: LogTrans(parseLog(dCM.L629, p1, p2, amount, account)+" (LC:629)"); return;
            case 630: LogTrans(parseLog(dCM.L630, p1, p2, amount, account)+" (LC:630)"); return;
            case 631: LogTrans(parseLog(dCM.L631, p1, p2, amount, account)+" (LC:631)"); return;
            case 632: LogTrans(parseLog(dCM.L632, p1, p2, amount, account)+" (LC:632)"); return;
            case 633: LogTrans(parseLog(dCM.L633, p1, p2, amount, account)+" (LC:633)"); return;
            case 634: LogTrans(parseLog(dCM.L634, p1, p2, amount, account)+" (LC:634)"); return;
            case 635: LogTrans(parseLog(dCM.L635, p1, p2, amount, account)+" (LC:635)"); return;
            case 636: LogTrans(parseLog(dCM.L636, p1, p2, amount, account)+" (LC:636)"); return;
            case 637: LogTrans(parseLog(dCM.L637, p1, p2, amount, account)+" (LC:637)"); return;
            case 638: LogTrans(parseLog(dCM.L638, p1, p2, amount, account)+" (LC:638)"); return;
            case 639: LogTrans(parseLog(dCM.L639, p1, p2, amount, account)+" (LC:639)"); return;
            default: LogTrans("UNKNOWN TRANSACTION HAPPENED! LC:" + code);
            }
        }
    }
    
    public String parseLog(String message, String p1, String p2, String amount, String account){
        message = message.replace("<p1>", p1);
        message = message.replace("<p2>", p2);
        message = message.replace("<a>", amount);
        message = message.replace("<acc>", account);
        return message;
    }
    
    public String parseMessage(String Message, String player, String type,  double amount){
        String parsedMessage = "";
        int x = (int)Math.floor((jwreset - System.currentTimeMillis()) / 1000);
        int xm = (int)Math.floor(x / (60));
        String mins = String.valueOf(JWDelay);
        parsedMessage = ColorCode(Message);
        parsedMessage = parsedMessage.replace("<p>", player);
        parsedMessage = parsedMessage.replace("<m>", MoneyName);
        parsedMessage = parsedMessage.replace("<a>", String.valueOf(displayform.format(amount)));
        parsedMessage = parsedMessage.replace("<acc>", type);
        parsedMessage = parsedMessage.replace("<rank>", type);
        parsedMessage = parsedMessage.replace("<min>", mins);
        parsedMessage = parsedMessage.replace("<xmin>", String.valueOf(xm));
        return prefix()+parsedMessage;
    }
    
    public String ColorCode(String codereplace){
        codereplace = codereplace.replace("<black>", Colors.Black);
        codereplace = codereplace.replace("<blue>", Colors.Blue);
        codereplace = codereplace.replace("<darkpurple>", Colors.DarkPurple);
        codereplace = codereplace.replace("<gold>", Colors.Gold);
        codereplace = codereplace.replace("<gray>", Colors.Gray);
        codereplace = codereplace.replace("<green>", Colors.Green);
        codereplace = codereplace.replace("<lightblue>", Colors.LightBlue);
        codereplace = codereplace.replace("<lightgray>", Colors.LightGray);
        codereplace = codereplace.replace("<lightgreen>", Colors.LightGreen);
        codereplace = codereplace.replace("<lightpurple>", Colors.LightPurple);
        codereplace = codereplace.replace("<navy>", Colors.Navy);
        codereplace = codereplace.replace("<purple>", Colors.Purple);
        codereplace = codereplace.replace("<red>", Colors.Red);
        codereplace = codereplace.replace("<rose>", Colors.Rose);
        codereplace = codereplace.replace("<white>", Colors.White);
        codereplace = codereplace.replace("<yellow>", Colors.Yellow);
        return codereplace;
    }
    
    public String[] decolormessage(String codereplace){
        String[] messcheck = new String[2];
        String[] colorscount = codereplace.split("§");
        int i = colorscount.length;
        messcheck[1] = String.valueOf(i);
        codereplace = codereplace.replace(Colors.Black, "");
        codereplace = codereplace.replace(Colors.Blue, "");
        codereplace = codereplace.replace(Colors.DarkPurple, "");
        codereplace = codereplace.replace(Colors.Gold, "");
        codereplace = codereplace.replace(Colors.Gray, "");
        codereplace = codereplace.replace(Colors.Green, "");
        codereplace = codereplace.replace(Colors.LightBlue, "");
        codereplace = codereplace.replace(Colors.LightGray, "");
        codereplace = codereplace.replace(Colors.LightGreen, "");
        codereplace = codereplace.replace(Colors.LightPurple, "");
        codereplace = codereplace.replace(Colors.Navy, "");
        codereplace = codereplace.replace(Colors.Purple, "");
        codereplace = codereplace.replace(Colors.Red, "");
        codereplace = codereplace.replace(Colors.Rose, "");
        codereplace = codereplace.replace(Colors.White, "");
        codereplace = codereplace.replace(Colors.Yellow, "");
        messcheck[0] = codereplace;
        return messcheck;
    }
    
    public String prefix(){
        if(MBJ){
            return "§2[§fMoney§2]§f ";
        }
        else{
            return "§2[§fdCo§2]§f ";
        }
    }
    
    public void LogTrans(String Action){
        date = new Date();
        if (MySQL){
            Connection conn = null;
            PreparedStatement ps = null;
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                SQLConnError(SQLE);
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("INSERT INTO dConomyLog (Date,Time,Transaction) VALUES(?,?,?)", 1);
                    ps.setString(1, dateFormat.format(date));
                    ps.setString(2, dateFormatTime.format(date));
                    ps.setString(3, Action);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.LogTrans() ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        SQLConnError(SQLE);
                    }
                }
            }
        }else{
            String filename= direTrans + String.valueOf(dateFormat.format(date)) + TransLoc;
            File LogFile = new File(filename);
            if (!LogFile.exists()){ 
                try {
                    LogFile.createNewFile();
                } catch (IOException e) {
                    log.severe("[dConomy] - Unable to create new Log File!");
                }
            }
            try {
                FileWriter fw = new FileWriter(filename,true);
                fw.write("["+dateFormatTime.format(date)+"] "+Action+ System.getProperty("line.separator"));
                fw.close();  
            } catch (IOException e) {
                log.severe("[dConomy] - Unable to Log Transaction!");
            }
        }
    }
    
    public void iConvertion(){
        String iCoDirLoc = "iConomy/";
        String iCoSettingsLoc = "iConomy/settings.properties";
        String iCoBalancesLoc = "iConomy/balances.properties";
        File iCoDir = new File(iCoDirLoc);
        File iCoSet = new File(iCoSettingsLoc);
        File iCoBalances = new File(iCoBalancesLoc);
        if (iCoDir.exists()){
            if (iCoSet.exists()){
                PropertiesFile iCoSettings = new PropertiesFile(iCoSettingsLoc);
                String iDataBase = iCoSettings.getString("db");
                String iUserName = iCoSettings.getString("user");
                String iPassword = iCoSettings.getString("pass");
                if (iCoSettings.getBoolean("use-mysql")){
                    HashMap<String, Integer> iBalances = new HashMap<String, Integer>();
                    Connection conn = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {
                        conn = DriverManager.getConnection(iDataBase, iUserName, iPassword);
                        ps = conn.prepareStatement("SELECT player,balance FROM iBalances ORDER BY player DESC");
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            iBalances.put(rs.getString("player"), Integer.valueOf(rs.getString("balance")));
                        }
                    } catch (SQLException ex) {
                        log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.iConvertion() ", ex);
                    }finally{
                        try{
                            if(rs != null && !rs.isClosed()){ rs.close(); }
                            if(ps != null && !ps.isClosed()){ ps.close(); }
                            if(conn != null && !conn.isClosed()){ conn.close(); }
                        }catch(SQLException SQLE){
                            SQLConnError(SQLE);
                        }
                    }
                    for(String name : iBalances.keySet()){
                        setInitialBalance(iBalances.get(name), name);
                    }
                    log.info("[dConomy] - iConomy Balances Converted!");
                }else{
                    if(iCoBalances.exists()){
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(iCoBalances));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                if(!line.contains("#")){
                                    String[] account = line.split("=");
                                    setInitialBalance(Double.valueOf(account[1]), account[0]);
                                }
                            }
                            reader.close();
                            log.info("[dConomy] - iConomy Balances Converted!");
                        }catch (IOException e){
                            log.severe("[dConomy] - Unable to Read iConomy/balances.properties");
                            return;
                        }
                    }
                }
            }
        }
        iConvert = false;
        CreatePropsFile(true);
    }
    
    public Map<String, Double> returnMap(String type) throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        if(MySQL){
            String Column = getMySQLColumn(type);
            Connection conn = getSQLConn();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                ps = conn.prepareStatement("SELECT Player,"+Column+" FROM dConomy");
                rs = ps.executeQuery();
                while(rs.next()){
                    map.put(rs.getString("Player"), rs.getDouble(Column));
                }
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "[dConomy] MySQL exception in dCData.returnMap() ", ex);
            }finally{
                try{
                    if(rs != null && !rs.isClosed()){ rs.close(); }
                    if(ps != null && !ps.isClosed()){ ps.close(); }
                    if(conn != null && !conn.isClosed()){ conn.close(); }
                }catch(SQLException SQLE){
                    SQLConnError(SQLE);
                }
            }
        }
        else{
            String file = getFile(type);
            BufferedReader reader = new BufferedReader(new FileReader(dire+file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                int delimPosition = line.indexOf('=');
                String key = line.substring(0, delimPosition).trim();
                double value = Double.valueOf(line.substring(delimPosition + 1).trim());
                map.put(key, value);
            }
            reader.close();
        }
        return map;
    }
    
    private void SQLConnError(SQLException SQLE){
        log.log(Level.SEVERE, "[dConomy] - Unable to set MySQL Connection ", SQLE);
        server.messageAll("§cAn error has occured in §2dConomy§c!");
    }
    
    private void SQLError(SQLException SQLE, String Method){
        log.log(Level.SEVERE, "[dConomy] MySQL exception in: "+Method, SQLE);
        server.messageAll("§cAn error has occured in §2dConomy§c!");
    }
    
    private void FileLoadError(Exception E, String File){
        log.log(Level.SEVERE, "[dConomy] - Unable to Load from File "+File, E);
        server.messageAll("§cAn error has occured in §2dConomy§c!");
    }
    
    private void FileSaveError(Exception E, String File){
        log.log(Level.SEVERE, "[dConomy] - Unable to Save to File "+File, E);
        server.messageAll("§cAn error has occured in §2dConomy§c!");
    }
}

/*******************************************************************************\
* dConomy                                                                       *
* Copyright (C) 2011-2012 Visual Illusions Entertainment                        *
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of dConomy.                                                 *                       
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
* GNU General Public License for more details.                                  *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.   *
\*******************************************************************************/
