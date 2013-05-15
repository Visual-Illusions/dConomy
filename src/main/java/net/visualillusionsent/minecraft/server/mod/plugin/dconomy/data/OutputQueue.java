package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

public final class OutputQueue{

    private LinkedList<Account> queue;

    public OutputQueue(){
        queue = new LinkedList<Account>();
    }

    public final void add(Account account){
        synchronized (queue) {
            queue.add(account);
            queue.notify();
        }
    }

    public final Account next(){
        Account account = null;
        if (queue.isEmpty()) {
            synchronized (queue) {
                try {
                    queue.wait();
                }
                catch (InterruptedException e) {
                    return null;
                }
            }
        }
        try {
            account = queue.getFirst();
            queue.removeFirst();
        }
        catch (NoSuchElementException nsee) {
            throw new InternalError("Race hazard in LinkedList object.");
        }
        return account;
    }

    public final void clear(){
        synchronized (queue) {
            queue.clear();
        }
    }
}
