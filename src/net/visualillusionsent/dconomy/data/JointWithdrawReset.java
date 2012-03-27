package net.visualillusionsent.dconomy.data;

public class JointWithdrawReset implements Runnable {
    public void run(){
        DataSource ds = DCoProperties.getDS();
        try{
            synchronized(ds.jointmap){
                for(String acc : ds.jointmap.keySet()){
                    ds.jointmap.get(acc).clearJUWD();
                }
            }
        }
        catch(Exception e){}
        ds.logger.info("[dConomy] - Joint User Withdraw Delay Reset!");
    }
}
