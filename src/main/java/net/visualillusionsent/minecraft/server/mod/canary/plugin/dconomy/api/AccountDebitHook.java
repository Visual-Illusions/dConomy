package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.hook.Hook;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;

public abstract class AccountDebitHook extends Hook{
    private final Mod_User sender;
    private final Mod_User recipient;
    private final double debit;
    private String error;

    public AccountDebitHook(Plugin plugin, Mod_User recipient, double debit){
        this.sender = new Canary_Plugin(plugin);
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
