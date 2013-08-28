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
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.plugin.Plugin;

/**
 * Wallet Deposit Hook<br>
 * Plugins should call this Hook to deposit into wallet accounts
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class WalletDepositHook extends AccountDepositHook{

    /**
     * Constructs a new WalletDepositHook
     * 
     * @param caller
     *            the {@link Plugin} giving money
     * @param username
     *            the user's name who is having money deposited
     * @param deposit
     *            the amount to be deposited
     */
    public WalletDepositHook(Plugin caller, String username, double deposit){
        super(caller, username, deposit);
    }

}
