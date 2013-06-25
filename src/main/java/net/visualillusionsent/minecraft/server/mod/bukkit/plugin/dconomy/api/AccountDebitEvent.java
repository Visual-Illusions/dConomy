/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2013 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.Bukkit_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

/**
 * Account Debit Event<br>
 * dConomy Add-on should extend this class for their own Account instances
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public abstract class AccountDebitEvent extends Event{
    private final IModUser caller;
    private final String username;
    private final double debit;
    private String error;

    /**
     * Constructs a new AccountDebitEvent
     * 
     * @param caller
     *            the {@link Plugin} asking to take money
     * @param username
     *            the user's name who is having money taken
     * @param debit
     *            the amount to be removed
     */
    public AccountDebitEvent(Plugin caller, String username, double debit){
        this.caller = new Bukkit_Plugin(caller);
        this.username = username;
        this.debit = debit;
    }

    /**
     * Gets the {@link IModUser}(plugin) asking to take money
     * 
     * @return the {@link IModUser}(plugin)
     */
    public final IModUser getCaller(){
        return caller;
    }

    /**
     * Gets the user's name who is having money taken
     * 
     * @return the user's name
     */
    public final String getUserName(){
        return username;
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
     */
    public final void setErrorMessage(String error){
        this.error = error;
    }
}
