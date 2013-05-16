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

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Wallet Set Balance Event<br>
 * Plugins should call this Event to set wallet account balances
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class WalletSetBalanceEvent extends AccountSetBalanceEvent{
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new WalletSetBalanceEvent
     * 
     * @param plugin
     *            the {@link Plugin} setting the balance
     * @param recipient
     *            the {@link Mod_User} who is having their balance set
     * @param toSet
     *            the amount to set the account to
     */
    public WalletSetBalanceEvent(Plugin plugin, Mod_User recipient, double toRemove){
        super(plugin, recipient, toRemove);
    }

    // Bukkit Event methods
    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
    //

}
