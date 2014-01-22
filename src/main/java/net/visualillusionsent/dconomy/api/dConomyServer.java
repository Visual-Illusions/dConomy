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
package net.visualillusionsent.dconomy.api;

import net.visualillusionsent.dconomy.api.account.AccountTransaction;

import java.util.logging.Logger;

/** dConomy Server interface */
public interface dConomyServer extends dConomyUser {

    /**
     * Gets a {@link dConomyUser} for the specified name
     *
     * @param name
     *         the name of the {@link dConomyUser}
     *
     * @return {@link dConomyUser} if found; {@code null} otherwise
     */
    dConomyUser getUser(String name);

    /**
     * Gets the Logger for the Server
     *
     * @return the logger
     */
    Logger getServerLogger();

    /**
     * Sends out notification of an AccountTransaction using the hook/event systems
     *
     * @param transaction
     *         the transaction that occurred
     */
    void newTransaction(AccountTransaction transaction);

    /**
     * Registers a Transaction Hook/Event
     *
     * @param clazz
     *         the Class of that extends AccountTransaction(Hook/Event)
     * @param transaction
     *         the Class that extends AccountTransaction
     */
    void registerTransactionHandler(Class<? extends TransactionHookEvent> clazz, Class<? extends AccountTransaction> transaction);

    /**
     * Unregisters a Transaction Hook/Event
     *
     * @param clazz
     *         the Class of that extends AccountTransaction(Hook/Event)
     */
    void unregisterTransactionHandler(Class<? extends TransactionHookEvent> clazz);

    dConomyAddOn getPluginAsAddOn(String pluginName) throws InvalidPluginException;
}
