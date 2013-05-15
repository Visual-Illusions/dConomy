package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.hook.Hook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

/**
 * Account Transaction Hook <br>
 * Called when an Account balance changes
 * 
 * @author Jason (darkdiplomat)
 */
public abstract class AccountTransactionHook extends Hook{
    protected final AccountTransaction action;

    public AccountTransactionHook(AccountTransaction action){
        this.action = action;
    }

    public abstract AccountTransaction getTransaction();

}