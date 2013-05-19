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
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;

/**
 * Account Balance request Hook<br>
 * dConomy Add-on should extend this class for their own Account instances
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public abstract class AccountBalanceHook extends Hook{
    private final IModUser caller;
    private final String username;
    private double balance = -1;
    private String error;

    /**
     * Constructs a new AccountBalanceHook
     * 
     * @param caller
     *            the {@link Plugin} requesting a balance
     * @param username
     *            the user's name to get a balance for
     */
    public AccountBalanceHook(Plugin caller, String username){
        this.caller = new Canary_Plugin(caller);
        this.username = username;
    }

    /**
     * Gets the {@link IModUser}(Plugin) requesting Balance information.
     * 
     * @return the {@link IModUser} caller
     */
    public final IModUser getCaller(){
        return caller;
    }

    /**
     * Gets the user's name who's balance is being requested for
     * 
     * @return the user's name
     */
    public final String getUserName(){
        return username;
    }

    /**
     * Gets the balance of the user's account
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
     */
    public final void setErrorMessage(String error){
        this.error = error;
    }
}
