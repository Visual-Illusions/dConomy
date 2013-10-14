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
 * Wallet Set Balance Hook<br>
 * Plugins should call this Hook to set wallet account balances
 *
 * @author Jason (darkdiplomat)
 */
public final class WalletSetBalanceHook extends AccountSetBalanceHook {

    /**
     * Constructs a new WalletSetBalanceHook
     *
     * @param caller   the {@link Plugin} setting the balance
     * @param username the user's name who is having their balance set
     * @param toSet    the amount to set the account to
     */
    public WalletSetBalanceHook(Plugin caller, String username, double toSet) {
        super(caller, username, toSet);
    }

}
