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

import net.canarymod.plugin.Plugin;

/**
 * Wallet Balance request Hook<br>
 * Plugins should call this Hook for WalletBalance details
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class WalletBalanceHook extends AccountBalanceHook{

    /**
     * Constructs a new WalletBalaceHook
     * 
     * @param plugin
     *            the {@link Plugin} requesting balance information
     * @param username
     *            the user's name to get balance for
     */
    public WalletBalanceHook(Plugin plugin, String username){
        super(plugin, username);
    }

}
