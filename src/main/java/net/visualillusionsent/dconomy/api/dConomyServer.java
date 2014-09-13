/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.api;

import net.visualillusionsent.dconomy.api.account.AccountTransaction;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * dConomy Server interface
 */
public interface dConomyServer extends dConomyUser {
    public static final UUID SERVERUUID = UUID.nameUUIDFromBytes("DCONOMY_SERVER".getBytes());

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
     * Gets a {@link dConomyUser} for the specified {@link UUID}
     *
     * @param uuid
     *         the {@link UUID} of the {@link dConomyUser}
     *
     * @return {@link dConomyUser} if found; {@code null} otherwise
     */
    dConomyUser getUserFromUUID(UUID uuid);

    /**
     * Gets the names of players known to the server
     *
     * @return user names
     */
    String[] getUserNames();

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

    UUID getUUIDFromName(String name);
}
