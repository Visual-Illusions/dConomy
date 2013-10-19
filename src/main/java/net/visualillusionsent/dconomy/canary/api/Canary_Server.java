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

import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.logger.CanaryLevel;
import net.canarymod.logger.Logman;
import net.visualillusionsent.dconomy.api.AccountTransaction;
import net.visualillusionsent.dconomy.api.TransactionHookEvent;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.CanarydConomy;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Canary Server wrapper for Mod_Server implementation
 *
 * @author Jason (darkdiplomat)
 */
public class Canary_Server implements dConomyServer, dConomyUser {

    private final Server serv;
    private final CanarydConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionHook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, CanarydConomy dCo) {
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends AccountTransactionHook>, Class<? extends AccountTransaction>>();
    }

    /** {@inheritDoc} */
    @Override
    public final dConomyUser getUser(String name) {
        if (name.equals("SERVER")) {
            return this;
        }

        Player player = serv.matchPlayer(name);
        if (player != null) {
            return new Canary_User(player);
        }
        else {
            OfflinePlayer offplayer = serv.getOfflinePlayer(name);
            if (offplayer != null) {
                return new Canary_OfflineUser(offplayer);
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final Logger getServerLogger() {
        return dCo.getLogman();
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return "SERVER";
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void error(String message) {
        getServerLogger().log(CanaryLevel.SERVERMESSAGE, message);
    }

    /** {@inheritDoc} */
    @Override
    public void message(String message) {
        getServerLogger().log(CanaryLevel.NOTICE, message);
    }

    /** {@inheritDoc} */
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
                    ((Logman) getServerLogger()).logStacktrace("Exception occurred while calling AccountTransactionHook", ex);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void registerTransactionHandler(Class<? extends TransactionHookEvent> clazz, Class<? extends AccountTransaction> transaction) {
        if (AccountTransactionHook.class.isAssignableFrom(clazz)) {
            transactions.putIfAbsent(clazz.asSubclass(AccountTransactionHook.class), transaction);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterTransactionHandler(Class<? extends TransactionHookEvent> clazz) {
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
