/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.hook.Hook;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;

/**
 * Account Debit Hook<br>
 * dConomy Add-on should extend this class for their own Account instances
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public abstract class AccountDebitHook extends Hook{
    private final Mod_User sender;
    private final Mod_User recipient;
    private final double debit;
    private String error;

    /**
     * Constructs a new AccountDebitHook
     * 
     * @param plugin
     *            the {@link Plugin} asking to take money
     * @param recipient
     *            the {@link Mod_User} who is having money taken
     * @param debit
     *            the amount to be removed
     */
    public AccountDebitHook(Plugin plugin, Mod_User recipient, double debit){
        this.sender = new Canary_Plugin(plugin);
        this.recipient = recipient;
        this.debit = debit;
    }

    /**
     * Gets the {@link Mod_User}(plugin) asking to take money
     * 
     * @return the {@link Mod_User}(plugin)
     */
    public final Mod_User getSender(){
        return sender;
    }

    /**
     * Gets the {@link Mod_User} who is having money taken
     * 
     * @return the {@link Mod_User}
     */
    public final Mod_User getRecipient(){
        return recipient;
    }

    /**
     * Gets the amount being removed
     * 
     * @return the debit amount
     */
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

    /**
     * Internal use method to set the error message should one have occurred
     * 
     * @param aex
     *            the AccountingException thrown
     */
    public final void setResult(AccountingException aex){
        this.error = aex.getMessage();
    }
}
