package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

public class dCoDataHandler{

    private final OutputQueue queue;
    private final dCoDataSource source;
    private final OutputThread outThread;

    public dCoDataHandler(DataSourceType type) throws DataSourceException{
        if(type == DataSourceType.XML){
            testJDOM();
            source = new WalletXMLSource();
        }
        else if(type == DataSourceType.MYSQL){
            testMySQLDriver();
            //source = new MySQL_Source();
            source = new WalletXMLSource();
        }
        else if(type == DataSourceType.SQLITE){
            testSQLiteDriver();
            //source = new SQLite_Source();
            source = new WalletXMLSource();
        }
        else{
            throw new DataSourceException("Invaild DataSourceType...");
        }
        source.load();
        queue = new OutputQueue();
        outThread = new OutputThread(this, source);
        outThread.start();
    }

    public void addToQueue(Account account){
        queue.add(account);
    }

    OutputQueue getQueue(){
        return queue;
    }

    private void clearQueue(){
        queue.clear();
    }

    public void killOutput(){
        clearQueue();
        outThread.terminate();
        //if(source.getType() == DataSourceType.SQLITE){
        //    ((SQLite_Source)source).closeDatabase();
        //}
    }

    private final void testJDOM() throws DataSourceException{
        try{
            Class.forName("org.jdom2.JDOMException");
        }
        catch(ClassNotFoundException cnfe){
            throw new DataSourceException(cnfe, DataSourceType.XML);
        }
    }

    private final boolean canFindSQLDriver(String driver){
        Enumeration<Driver> en = DriverManager.getDrivers();
        while(en.hasMoreElements()){
            Driver drive = en.nextElement();
            if(drive.getClass().getName().equals(driver)){
                return true;
            }
        }
        return false;
    }

    private final void testSQLiteDriver() throws DataSourceException{
        if(!canFindSQLDriver("org.sqlite.JDBC")){
            try{
                Class.forName("org.sqlite.JDBC");
            }
            catch(ClassNotFoundException cnfe){
                throw new DataSourceException(cnfe, DataSourceType.SQLITE);
            }
        }
    }

    private final void testMySQLDriver() throws DataSourceException{
        if(!canFindSQLDriver("com.mysql.jdbc.Driver")){
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch(ClassNotFoundException cnfe){
                throw new DataSourceException(cnfe, DataSourceType.MYSQL);
            }
        }
    }
}
