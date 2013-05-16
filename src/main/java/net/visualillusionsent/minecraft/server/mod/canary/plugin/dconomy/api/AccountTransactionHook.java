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
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

/**
 * Account Transaction Hook <br>
 * Called when an Account balance changes<br>
 * dConomy Add-on should extend this class for their own Account instances<br>
 * 
 * @author Jason (darkdiplomat)
 */
public abstract class AccountTransactionHook extends Hook{
    protected final AccountTransaction action;

    /**
     * Constructs a new Account Transaction Hook
     * 
     * @param action
     *            the AccountTransaction done
     */
    public AccountTransactionHook(AccountTransaction action){
        this.action = action;
    }

    /**
     * Returns the {@link AccountTransaction} that happen
     * 
     * @return {@link AccountTransaction}
     */
    public abstract AccountTransaction getTransaction();

}
