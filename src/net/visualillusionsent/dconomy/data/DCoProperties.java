package net.visualillusionsent.dconomy.data;

/**
 * dCoProperties.java - dConomy Properties handler class
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class DCoProperties {
    private static double accinitial;
    private static String dir, moneyname;
    private static DataSource ds;
    
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
}
