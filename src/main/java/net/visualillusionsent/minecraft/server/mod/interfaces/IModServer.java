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
package net.visualillusionsent.minecraft.server.mod.interfaces;

import java.util.logging.Logger;
import net.visualillusionsent.minecraft.server.mod.dconomy.accounting.AccountTransaction;

public interface IModServer{

    /**
     * Gets a {@link IModUser} for the specified name
     * 
     * @param name
     *            the name of the {@link IModUser}
     * @return {@link IModUser} if found; {@code null} otherwise
     */
    IModUser getUser(String name);

    /**
     * Gets the Logger for the Server
     * 
     * @return the logger
     */
    Logger getServerLogger();

    /**
     * Gets the ModType of the Server
     * 
     * @return the ModType
     * @see ModType
     */
    ModType getModType();

    /**
     * Sends out notification of an AccountTransaction using the hook/event systems
     * 
     * @param transaction
     *            the transaction that occurred
     */
    void newTransaction(AccountTransaction transaction);

    /**
     * Registers a Transaction Hook/Event
     * 
     * @param clazz
     *            the Class of that extends AccountTransaction(Hook/Event)
     * @param transaction
     *            the Class that extends AccountTransaction
     */
    void registerTransactionHandler(Class<?> clazz, Class<? extends AccountTransaction> transaction);

    /**
     * Unregisters a Transaction Hook/Event
     * 
     * @param clazz
     *            the Class of that extends AccountTransaction(Hook/Event)
     */
    void deregisterTransactionHandler(Class<?> clazz);
}
