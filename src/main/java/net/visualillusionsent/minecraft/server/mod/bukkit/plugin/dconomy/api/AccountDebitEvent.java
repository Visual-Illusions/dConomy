package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.Bukkit_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public abstract class AccountDebitEvent extends Event{
    private final Mod_User sender;
    private final Mod_User recipient;
    private final double debit;
    private String error;

    public AccountDebitEvent(Plugin plugin, Mod_User recipient, double debit){
        this.sender = new Bukkit_Plugin(plugin);
        this.recipient = recipient;
        this.debit = debit;
    }

    public final Mod_User getSender(){
        return sender;
    }

    public final Mod_User getRecipient(){
        return recipient;
    }

    public final double getDebit(){
        return debit;
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
