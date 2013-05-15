package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

final class OutputThread extends Thread{

    private final dCoDataHandler handler;
    private volatile boolean running = true;

    public OutputThread(dCoDataHandler handler){
        this.handler = handler;
    }

    public void run(){
        while (running) {
            try {
                Account account = handler.getQueue().next();
                account.getDataSource().saveAccount(account);
            }
            catch (Exception e) {}
        }
    }

    public final void terminate(){
        running = false;
        interrupt();
    }
}
