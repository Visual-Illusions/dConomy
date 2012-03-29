package net.visualillusionsent.dconomy.data;

/**
 * Runs to pay user's bank interest (if enabled)
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class BankInterestCaller implements Runnable {

    public void run() {
        DataSource ds = DCoProperties.getDS();
        try{
            synchronized(ds.bankmap){
                for(String acc : ds.bankmap.keySet()){
                    double bal = ds.bankmap.get(acc);
                    double interest = bal + (bal*DCoProperties.getBankInterest());
                    ds.bankmap.put(acc, interest);
                }
            }
        }
        catch(Exception e){}
        ds.breset = (System.currentTimeMillis() + (DCoProperties.getBankDelay() * 60 * 1000));
        ds.reseter.setProperty("BankTimerResetTo", String.valueOf(ds.breset));
        ds.logger.info("[dConomy] - Bank Interest Paid!");
    }
}
