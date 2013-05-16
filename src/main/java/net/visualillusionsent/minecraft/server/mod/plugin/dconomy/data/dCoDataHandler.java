package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

public class dCoDataHandler{

    private final OutputQueue queue;
    private final OutputThread outThread;
    private final DataSourceType dataType;

    public dCoDataHandler(DataSourceType type) throws DataSourceException{
        dataType = type;
        if (type == DataSourceType.XML) {
            testJDOM();
        }
        else if (type == DataSourceType.MYSQL) {
            testMySQLDriver();
        }
        else if (type == DataSourceType.SQLITE) {
            testSQLiteDriver();
        }
        else {
            throw new DataSourceException("Invaild DataSourceType...");
        }
        queue = new OutputQueue();
        outThread = new OutputThread(this);
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
    }

    public final DataSourceType getDataSourceType(){
        return dataType;
    }

    private final void testJDOM() throws DataSourceException{
        try {
            Class.forName("org.jdom2.JDOMException");
        }
        catch (ClassNotFoundException cnfe) {
            throw new DataSourceException(cnfe, DataSourceType.XML);
        }
    }

    private final boolean canFindSQLDriver(String driver){
        Enumeration<Driver> en = DriverManager.getDrivers();
        while (en.hasMoreElements()) {
            Driver drive = en.nextElement();
            if (drive.getClass().getName().equals(driver)) {
                return true;
            }
        }
        return false;
    }

    private final void testSQLiteDriver() throws DataSourceException{
        if (!canFindSQLDriver("org.sqlite.JDBC")) {
            try {
                Class.forName("org.sqlite.JDBC");
            }
            catch (ClassNotFoundException cnfe) {
                throw new DataSourceException(cnfe, DataSourceType.SQLITE);
            }
        }
    }

    private final void testMySQLDriver() throws DataSourceException{
        if (!canFindSQLDriver("com.mysql.jdbc.Driver")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (ClassNotFoundException cnfe) {
                throw new DataSourceException(cnfe, DataSourceType.MYSQL);
            }
        }
    }
}
