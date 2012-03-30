package net.visualillusionsent.dconomy.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 * MySqlSource - class for handling MySQL Data
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 *
 * @see DataSource
 */
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
    
    private Connection getSQLConn() throws SQLException{
        Connection conn = DriverManager.getConnection(database, username, password);
        return conn;
    }
    
    boolean loadMaps() {
        super.loadMaps();
        if(!CreateTable()){
            return false;
        }
        logger.info("[dConomy] Attempting to load Accounts & Bank Accounts...");
        if(!loadAccounts()){
            return false;
        }
        logger.info("[dConomy] Attempting to load Joint Accounts...");
        if(!loadJointAccounts()){
            return false;
        }
        Scheduler();
        return true;
    }
    
    private boolean CreateTable(){
        String table1 = ("CREATE TABLE IF NOT EXISTS `dConomy` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Player` varchar(16) NOT NULL, `Account` DECIMAL(64,2) NOT NULL, `Bank` DECIMAL(64,2) NOT NULL, PRIMARY KEY (`ID`))");
        String table2 = ("CREATE TABLE IF NOT EXISTS `dConomyJoint` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Name` varchar(32) NOT NULL, `Owners` text NOT NULL, `Users` text NOT NULL, `Balance` DECIMAL(64,2) NOT NULL, `UserMaxWithdraw` DECIMAL(64,2) NOT NULL, `WithdrawDelay` INT(255) NOT NULL, `DelayReset` BIGINT(255) NOT NULL, PRIMARY KEY (`ID`))");
        String table3 = ("CREATE TABLE IF NOT EXISTS `dConomyLog` (`ID` INT(255) NOT NULL AUTO_INCREMENT, `Date` varchar(32) NOT NULL, `Time` varchar(32) NOT NULL, `Transaction` Text NOT NULL, PRIMARY KEY (`ID`))");
        
        String update1 = ("ALTER TABLE `dConomyJoint` ADD COLUMN `WithdrawDelay` INT(255) NOT NULL DEFAULT 30  AFTER `UserMaxWithdraw` , ADD COLUMN `DelayReset` BIGINT(255) NOT NULL DEFAULT -1  AFTER `WithdrawDelay` , ADD COLUMN `JointWithdrawDelayMap` TEXT NOT NULL  AFTER `DelayReset` ;");
        boolean toRet = true;
        try{
            conn = getSQLConn();
        }catch(SQLException SQLE){
            logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ CreateTable ", SQLE);
            logger.severe("dConomy will now be disabled");
            return false;
        }
        
        if(conn != null){
            try{
                st = conn.createStatement();
                st.addBatch(table1);
                st.addBatch(table2);
                st.addBatch(table3);
                st.executeBatch();
                try{
                    st.execute(update1);
                }
                catch(SQLException SQLE){
                    //Probably already updated
                }
            }catch (SQLException SQLE) {
                logger.log(Level.SEVERE, "[dConomy] Failed to create Tables ", SQLE);
                logger.severe("dConomy will now be disabled");
                toRet = false;
            }finally{
                try{
                    if(st != null && !st.isClosed()){ st.close(); }
                    if(conn != null && !conn.isClosed()){ conn.close(); }
                }catch(SQLException SQLE){
                    logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ CreateTable ", SQLE);
                }
            }
        }
        return toRet;
    }
    
    private boolean loadAccounts(){
        boolean toRet = true;
        synchronized(accmap){
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ loadAccounts ", SQLE);
                logger.severe("dConomy will now be disabled");
                return false;
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("SELECT * FROM dConomy");
                    rs = ps.executeQuery();
                    while (rs.next()){
                        String username = "Unknown(null?)";
                        try{
                            username = rs.getString("Player");
                            accmap.put(username, rs.getDouble("Account"));
                            bankmap.put(username, rs.getDouble("Bank"));
                        }
                        catch(Exception e){
                            logger.warning("[dConomy] There was an issue getting data for User: "+username+" Skipping...");
                            continue;
                        }
                    }
                    logger.info("[dConomy] Accounts & Bank Accounts loaded!");
                } 
                catch (SQLException ex) {
                    logger.log(Level.SEVERE, "[dConomy] MySQL exception while reading dConomy Table ", ex);
                    logger.severe("dConomy will now be disabled");
                    toRet = false;
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
        return toRet;
    }
    
    private boolean loadJointAccounts(){
        boolean toRet = true;
        synchronized(jointmap){
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ loadAccounts ", SQLE);
                logger.severe("dConomy will now be disabled");
                return false;
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("SELECT * FROM dConomyJoint");
                    rs = ps.executeQuery();
                    while (rs.next()){
                        String accname = "Unknown(null?)";
                        try{
                            accname = rs.getString("Name");
                            String[] owners = rs.getString("Owners").split(",");
                            String[] users = rs.getString("Users").split(",");
                            double bal = rs.getDouble("Balance");
                            double max = rs.getDouble("UserMaxWithdraw");
                            int delay = rs.getInt("WithdrawDelay");
                            long reset = rs.getLong("DelayReset");
                            String juwd = rs.getString("JointWithdrawDelayMap");
                            
                            JointAccount joint = new JointAccount(users, owners, bal, max, delay, reset, juwd);
                            jointmap.put(accname, joint);
                        }
                        catch(Exception e){
                            logger.warning("[dConomy] There was an issue getting data for JointAccount: "+accname+" Skipping...");
                            continue;
                        }
                    }
                    logger.info("[dConomy] Joint Accounts loaded!");
                } 
                catch (SQLException ex) {
                    logger.log(Level.SEVERE, "[dConomy] MySQL exception while reading dConomy Table ", ex);
                    logger.severe("dConomy will now be disabled");
                    toRet = false;
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
        return toRet;
    }
    
    public void saveMaps(){
        super.saveMaps();
        logger.info("[dConomy] Saving accounts...");
        ArrayList<String> update = new ArrayList<String>();
        ArrayList<String> insert = new ArrayList<String>();
        ArrayList<String> exists = new ArrayList<String>();
        ArrayList<String> delete = new ArrayList<String>();
        
        try{
            conn = getSQLConn();
        }
        catch(SQLException SQLE){
            logger.log(Level.SEVERE, "[dConomy] Failed to set MySQL Connection @ saveMaps ", SQLE);
            logger.info("[dConomy] Saving failed...");
            return;
        }
        try{
            synchronized(accmap){
                ps = conn.prepareStatement("SELECT * FROM dConomy");
                rs = ps.executeQuery();
                while (rs.next()){
                    exists.add(rs.getString("Player"));
                }
                
                for(String ex : exists){
                    if(accmap.containsKey(ex) && bankmap.containsKey(ex)){
                        update.add(ex);
                    }
                    else{
                        insert.add(ex);
                    }
                }
                
                ps = conn.prepareStatement("UPDATE dConomy SET Account = ?, Bank = ? WHERE Player = ?");
                for(String up : update){
                    ps.setDouble(1, accmap.get(up));
                    ps.setDouble(2, bankmap.get(up));
                    ps.setString(3, up);
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
                
                ps = conn.prepareStatement("INSERT INTO dConomy (Player, Account, Bank) VALUES(?,?,?)");
                for(String in : insert){
                    ps.setString(1, in);
                    ps.setDouble(2, accmap.get(in));
                    ps.setDouble(3, bankmap.get(in));
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
            }
            update.clear();
            insert.clear();
            exists.clear();
            synchronized(jointmap){
                System.out.println("Joint");
                ps = conn.prepareStatement("SELECT * FROM dConomyJoint");
                rs = ps.executeQuery();
                while (rs.next()){
                    exists.add(rs.getString("Name"));
                }
                
                for(String ex : exists){
                    if(!jointmap.containsKey(ex)){
                        delete.add(ex);
                    }
                }
                for(String key : jointmap.keySet()){
                    if(exists.contains(key)){
                        update.add(key);
                    }
                    else{
                        insert.add(key);
                    }
                }
                
                ps = conn.prepareStatement("UPDATE dConomyJoint SET Owners = ?, Users = ?, Balance = ?, UserMaxWithdraw = ?, WithdrawDelay = ?, DelayReset = ?, JointWithdrawDelayMap = ? WHERE Name = ?");
                for(String up : update){
                    JointAccount joint = jointmap.get(up);
                    ps.setString(1, joint.getOwners());
                    ps.setString(2, joint.getUsers());
                    ps.setDouble(3, joint.getBalance());
                    ps.setDouble(4, joint.getMaxUserWithdraw());
                    ps.setInt(5, joint.getDelay());
                    ps.setLong(6, joint.getReset());
                    ps.setString(7, joint.getJUWDMap());
                    ps.setString(8, up);
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
                
                ps = conn.prepareStatement("INSERT INTO dConomyJoint (Name, Owners, Users, Balance, UserMaxWithdraw, WithdrawDelay, DelayReset, JointWithdrawDelayMap) VALUES (?,?,?,?,?,?,?,?)");
                for(String in : insert){
                    JointAccount joint = jointmap.get(in);
                    ps.setString(1, in);
                    ps.setString(2, joint.getOwners());
                    ps.setString(3, joint.getUsers());
                    ps.setDouble(4, joint.getBalance());
                    ps.setDouble(5, joint.getMaxUserWithdraw());
                    ps.setInt(6, joint.getDelay());
                    ps.setLong(7, joint.getReset());
                    ps.setString(8, joint.getJUWDMap());
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
                
                ps = conn.prepareStatement("DELETE FROM dConomyJoint WHERE Name = ?");
                for(String del : delete){
                    ps.setString(1, del);
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
            }
        }
        catch(SQLException SQLE){
            logger.log(Level.SEVERE, "[dConomy] Failed to save accounts... ", SQLE);
        }
        finally{
            try{
                if(rs != null && !rs.isClosed()){ rs.close(); }
                if(ps != null && !ps.isClosed()){ ps.close(); }
                if(conn != null && !conn.isClosed()){ conn.close(); }
            }
            catch(SQLException SQLE){
                logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ saveMaps ", SQLE);
            }
        }
        logger.info("[dConomy] Saving complete.");
    }
    
    public void logTrans(String action){
        if(DCoProperties.isLogging()){
            date = new Date();
            try{
                conn = getSQLConn();
            }catch(SQLException SQLE){
                logger.log(Level.WARNING, "[dConomy] Failed to get MySQL Connection @ logTrans ", SQLE);
                return;
            }
            if(conn != null){
                try{
                    ps = conn.prepareStatement("INSERT INTO dConomyLog (Date,Time,Transaction) VALUES(?,?,?)", 1);
                    ps.setString(1, dateFormat.format(date));
                    ps.setString(2, dateFormatTime.format(date));
                    ps.setString(3, action);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "[dConomy] MySQL exception @ logTrans ", ex);
                }finally{
                    try{
                        if(ps != null && !ps.isClosed()){ ps.close(); }
                        if(conn != null && !conn.isClosed()){ conn.close(); }
                    }catch(SQLException SQLE){
                        logger.log(Level.WARNING, "[dConomy] Failed to close MySQL Connection @ logTrans ", SQLE);
                    }
                }
            }
        }
    }
}
