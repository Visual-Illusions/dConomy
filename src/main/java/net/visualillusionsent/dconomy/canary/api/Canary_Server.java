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
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.api.PlayerReference;
import net.canarymod.api.Server;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.dconomy.api.*;
import net.visualillusionsent.dconomy.api.account.AccountTransaction;
import net.visualillusionsent.dconomy.canary.CanaryConomy;
import net.visualillusionsent.dconomy.canary.api.account.AccountTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Canary Server wrapper for Mod_Server implementation
 *
 * @author Jason (darkdiplomat)
 */
public class Canary_Server implements dConomyServer, dConomyUser {

    private final Server serv;
    private final CanaryConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionHook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, CanaryConomy dCo) {
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends AccountTransactionHook>, Class<? extends AccountTransaction>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final dConomyUser getUser(String name) {
        if (name.toUpperCase().equals("SERVER")) {
            return this;
        }

        PlayerReference player = serv.matchKnownPlayer(name);
        if (player != null) {
            return new Canary_User(player);
        }
        return null;
    }

    @Override
    public final String[] getUserNames() {
        return Canary.getServer().getKnownPlayerNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Logger getServerLogger() {
        //return dCo.getLogman();
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "SERVER";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UUID getUUID() {
        return SERVERUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(String message) {
        Canary.getServer().message(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(String message) {
        Canary.getServer().notice(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newTransaction(AccountTransaction transaction) {
        for (Class<? extends AccountTransactionHook> clazz : transactions.keySet()) {
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionHook hook = clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Canary.hooks().callHook(hook);
                    break;
                }
                catch (Exception ex) {
                    //getServerLogger().error("Exception occurred while calling AccountTransactionHook", ex);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerTransactionHandler(Class<? extends TransactionHookEvent> clazz, Class<? extends AccountTransaction> transaction) {
        if (AccountTransactionHook.class.isAssignableFrom(clazz)) {
            transactions.putIfAbsent(clazz.asSubclass(AccountTransactionHook.class), transaction);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterTransactionHandler(Class<? extends TransactionHookEvent> clazz) {
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }

    @Override
    public dConomyAddOn getPluginAsAddOn(String pluginName) throws InvalidPluginException {
        Plugin plugin = Canary.loader().getPlugin(pluginName);
        if (plugin == null) {
            throw new InvalidPluginException("Plugin not found");
        }
        return new Canary_Plugin(plugin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }

    public final boolean isOnline() {
        return true;
    }

    public final UUID getUUIDFromName(String name) {
        String possible = ToolBox.usernameToUUID(name);
        return possible != null ? UUID.fromString(possible) : null;
    }
}
