package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;
import org.bukkit.event.Event;

/**
 * Account Transaction Event <br>
 * Called when an Account balance changes
 * 
 * @author Jason (darkdiplomat)
 */
public abstract class AccountTransactionEvent extends Event{
    protected final AccountTransaction action;

    public AccountTransactionEvent(AccountTransaction action){
        this.action = action;
    }

    public abstract AccountTransaction getTransaction();
}
