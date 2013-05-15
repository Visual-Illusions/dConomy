package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.hook.Hook;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;

public abstract class AccountBalanceHook extends Hook{
    private final Mod_User plugin;
    private final Mod_User user;
    private double balance = -1;
    private String error;

    public AccountBalanceHook(Plugin plugin, Mod_User user){
        this.plugin = new Canary_Plugin(plugin);
        this.user = user;
    }

    public final Mod_User getRequester(){
        return plugin;
    }

    public final Mod_User getUser(){
        return user;
    }

    public final double getBalance(){
        return balance;
    }

    public final void setBalance(double balance){
        this.balance = balance;
    }

    /**
     * Gets the error message if an error has occurred
     * 
     * @return {@code null} if no error occurred; The error message otherwise
     */
    public final String getErrorMessage(){
        return error;
    }

    public final void setResult(AccountingException aex){
        this.error = aex.getMessage();
    }

}
