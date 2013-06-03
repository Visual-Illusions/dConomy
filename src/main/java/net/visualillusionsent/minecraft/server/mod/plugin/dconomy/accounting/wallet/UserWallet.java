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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletDataSource;

/**
 * User Wallet container
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class UserWallet extends Wallet{

    /**
     * Constructs a new UserWallet and adds it to the {@link WalletHandler}
     * 
     * @param owner
     *            the owner's name
     * @param balance
     *            the current balance
     * @param locked
     *            whether or not the Wallet is locked out
     * @param source
     *            the {@link WalletDataSource} used to store the Wallet
     */
    public UserWallet(String owner, double balance, boolean locked, WalletDataSource source){
        super(owner, balance, locked, source);
        WalletHandler.addWallet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void save(){
        dCoBase.getDataHandler().addToQueue(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reload(){
        return ((WalletDataSource) datasource).reloadAccount(this);
    }
}
