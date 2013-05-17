package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

final class OutputThread extends Thread{

    private final dCoDataHandler handler;
    private volatile boolean running = true;

    public OutputThread(dCoDataHandler handler){
        this.handler = handler;
    }

    public void run(){
        while (running) {
            Account account = null;
            try {
                account = handler.getQueue().next();
                if (account.getDataSource() != null) {
                    account.getDataSource().saveAccount(account);
                }
            }
            catch (Exception ex) {
                dCoBase.severe("Exception occurred in OutputThread for Account: " + (account != null ? account.getClass().getName() : "UNKNOWN ACCOUNT CLASS"));
                dCoBase.stacktrace(ex);
            }
        }
    }

    public final void terminate(){
        running = false;
        interrupt();
    }
}
