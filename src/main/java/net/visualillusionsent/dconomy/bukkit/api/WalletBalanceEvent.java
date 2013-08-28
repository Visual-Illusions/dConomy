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
package net.visualillusionsent.dconomy.bukkit.api;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * Wallet Balance request Event<br>
 * Plugins should call this Event for WalletBalance details
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class WalletBalanceEvent extends AccountBalanceEvent{
    private static final HandlerList handlers = new HandlerList();

    /**
     * Constructs a new WalletBalaceEvent
     * 
     * @param caller
     *            the {@link Plugin} requesting balance information
     * @param username
     *            the user's name to get balance for
     */
    public WalletBalanceEvent(Plugin caller, String username){
        super(caller, username);
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
