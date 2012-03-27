package net.visualillusionsent.dconomy.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;

public class MySqlSource extends DataSource{
    private Connection conn;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;
    private String database, username, password;
    
    MySqlSource(String database, String username, String password){
        this.database = database;
        this.username = username;
        this.password = password;
    }
    
    public Connection getSQLConn() throws SQLException{
        Connection conn = DriverManager.getConnection(database, username, password);
        return conn;
    }
    
    void loadMaps() {
        CreateTable();
        loadAccounts();
        loadJointAccounts();
    }
    
    private void CreateTable(){
        String table1 = ("CREATE TABLE IF NOT EXISTS `dConomy` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Player` varchar(16) NOT NULL, `Account` DECIMAL(64,2) NOT NULL, `Bank` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
        String table2 = ("CREATE TABLE IF NOT EXISTS `dConomyJoint` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Name` varchar(32) NOT NULL, `Owners` text NOT NULL, `Users` text NOT NULL, `Balance` DECIMAL(64,2) NOT NULL, `UserMaxWithdraw` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
        String table3 = ("CREATE TABLE IF NOT EXISTS `dConomyLog` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Date` varchar(32) NOT NULL, `Time` varchar(32) NOT NULL, `Transaction` Text NOT NULL, PRIMARY KEY (`ID`))");
        
        try{
            conn = getSQLConn();
        }catch(SQLException SQLE){
            logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ CreateTable ", SQLE);
            conn = null;
        }
        
        if(conn != null){
            try{
                st = conn.createStatement();
                st.addBatch(table1);
                st.addBatch(table2);
                st.addBatch(table3);
                st.executeBatch();
            }catch (SQLException SQLE) {
                logger.log(Level.SEVERE, "[dConomy] Failed to create Tables ", SQLE);
            }finally{
                try{
                    if(st != null && !st.isClosed()){ st.close(); }
                    if(conn != null && !conn.isClosed()){ conn.close(); }
                }catch(SQLException SQLE){
                    logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ CreateTable ", SQLE);
                }
            }
        }
    }
    
    private void loadAccounts(){
        synchronized(accmap){
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ loadAccounts ", SQLE);
                conn = null;
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("SELECT * FROM dConomy");
                    rs = ps.executeQuery();
                    while (rs.next()){
                        String username = rs.getString("Player");
                        accmap.put(username, rs.getDouble("Account"));
                        bankmap.put(username, rs.getDouble("Bank"));
                    }
                } 
                catch (SQLException ex) {
                    logger.log(Level.SEVERE, "[dConomy] MySQL exception while reading dConomy Table ", ex);
                }
                finally{
                    try{
                        if(rs != null && !rs.isClosed()){ rs.close(); }
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }
                    catch(SQLException SQLE){
                        logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ loadAccounts ", SQLE);
                    }
                }
            }
        }
    }
    
    private void loadJointAccounts(){
        synchronized(jointmap){
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ loadAccounts ", SQLE);
                conn = null;
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("SELECT * FROM dConomyJoint");
                    rs = ps.executeQuery();
                    while (rs.next()){
                        String accname = rs.getString("Name");
                        String[] owners = rs.getString("Owners").split(",");
                        String[] users = rs.getString("Users").split(",");
                        double bal = rs.getDouble("Balance");
                        double max = rs.getDouble("UserMaxWithdraw");
                        JointAccount joint = new JointAccount(owners, users, bal, max);
                        jointmap.put(accname, joint);
                    }
                } 
                catch (SQLException ex) {
                    logger.log(Level.SEVERE, "[dConomy] MySQL exception while reading dConomy Table ", ex);
                }
                finally{
                    try{
                        if(rs != null && !rs.isClosed()){ rs.close(); }
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }
                    catch(SQLException SQLE){
                        logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ loadAccounts ", SQLE);
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
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("INSERT INTO dConomyLog (Date,Time,Transaction) VALUES(?,?,?)", 1);
                    ps.setString(1, dateFormat.format(date));
                    ps.setString(2, dateFormatTime.format(date));
                    ps.setString(3, action);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "[dConomy] MySQL exception @ logTrans ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        
                    }
                }
            }
        }
    }
}
