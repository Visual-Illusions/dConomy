package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

final class OutputThread extends Thread{

    private final dCoDataHandler handler;
    private final dCoDataSource source;
    private volatile boolean running = true;

    public OutputThread(dCoDataHandler handler, dCoDataSource source){
        this.handler = handler;
        this.source = source;
    }

    public void run(){
        while(running){
            try{
                Account account = handler.getQueue().next();
                source.saveAccount(account);
            }
            catch(Exception e){}
        }
    }

    public final void terminate(){
        running = false;
        interrupt();
    }
}
