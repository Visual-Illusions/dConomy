/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.bukkit.api.account;

import net.visualillusionsent.dconomy.api.TransactionHookEvent;
import net.visualillusionsent.dconomy.api.account.AccountTransaction;
import org.bukkit.event.Event;

/**
 * Account Transaction Event <br>
 * Called when an Account balance changes<br>
 * dConomy Add-on should extend this class for their own Account instances<br>
 *
 * @author Jason (darkdiplomat)
 */
public abstract class AccountTransactionEvent extends Event implements TransactionHookEvent {
    protected final AccountTransaction action;

    /**
     * Constructs a new Account Transaction Event
     *
     * @param action
     *         the AccountTransaction done
     */
    public AccountTransactionEvent(AccountTransaction action) {
        this.action = action;
    }
}
