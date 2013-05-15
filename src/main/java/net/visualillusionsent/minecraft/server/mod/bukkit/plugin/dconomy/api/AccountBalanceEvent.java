package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.Bukkit_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public abstract class AccountBalanceEvent extends Event{
    private final Mod_User plugin;
    private final Mod_User user;
    private double balance = -1;
    private String error;

    public AccountBalanceEvent(Plugin plugin, Mod_User user){
        this.plugin = new Bukkit_Plugin(plugin);
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
