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
package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.Bukkit_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

/**
 * Account Balance request Event<br>
 * dConomy Add-on should extend this class for their own Account instances
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public abstract class AccountBalanceEvent extends Event{
    private final Mod_User plugin;
    private final Mod_User user;
    private double balance = -1;
    private String error;

    /**
     * Constructs a new AccountBalanceEvent
     * 
     * @param plugin
     *            the {@link Plugin} requesting a balance
     * @param user
     *            the {@link Mod_User} to get a balance for
     */
    public AccountBalanceEvent(Plugin plugin, Mod_User user){
        this.plugin = new Bukkit_Plugin(plugin);
        this.user = user;
    }

    /**
     * Gets the {@link Mod_User}(Plugin) requesting Balance information.
     * 
     * @return the {@link Mod_User} requester
     */
    public final Mod_User getRequester(){
        return plugin;
    }

    /**
     * Gets the {@link Mod_User} who's balance is being requested for
     * 
     * @return
     */
    public final Mod_User getUser(){
        return user;
    }

    /**
     * Gets the balance of the {@link Mod_User}'s account
     * 
     * @return the balance
     */
    public final double getBalance(){
        return balance;
    }

    /**
     * Internal use method to set the balance to be returned to the requester.
     * 
     * @param balance
     *            the balance of the account
     */
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
