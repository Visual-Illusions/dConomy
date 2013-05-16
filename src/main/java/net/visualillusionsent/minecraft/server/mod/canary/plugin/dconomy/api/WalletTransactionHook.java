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

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

/**
 * Wallet Transaction Hook <br>
 * Called when a Wallet balance changes
 * 
 * @author Jason (darkdiplomat)
 */
public final class WalletTransactionHook extends AccountTransactionHook{

    /**
     * Constructs a new Wallet Transaction Hook
     * 
     * @param action
     *            the {@link WalletTransaction} done
     */
    public WalletTransactionHook(WalletTransaction action){
        super(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final WalletTransaction getTransaction(){
        return (WalletTransaction) action;
    }

}
