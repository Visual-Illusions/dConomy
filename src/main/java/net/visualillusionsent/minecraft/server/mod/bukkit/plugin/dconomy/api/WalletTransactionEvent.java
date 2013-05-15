package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import org.bukkit.event.HandlerList;

/**
 * Wallet Transaction Hook <br>
 * Called when a Wallet balance changes
 * 
 * @author Jason (darkdiplomat)
 */
public final class WalletTransactionEvent extends AccountTransactionEvent{
    private static final HandlerList handlers = new HandlerList();

    public WalletTransactionEvent(WalletTransaction action){
        super(action);
    }

    public final WalletTransaction getTransaction(){
        return (WalletTransaction) action;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
