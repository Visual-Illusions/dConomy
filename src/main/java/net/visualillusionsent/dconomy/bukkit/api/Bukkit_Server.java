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
package net.visualillusionsent.dconomy.bukkit.api;

import net.canarymod.logger.Logman;
import net.visualillusionsent.dconomy.MessageTranslator;
import net.visualillusionsent.dconomy.api.AccountTransaction;
import net.visualillusionsent.dconomy.api.TransactionHookEvent;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.bukkit.BukkitdConomy;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bukkit Server wrapper for Mod_Server implementation
 *
 * @author Jason (darkdiplomat)
 */
public final class Bukkit_Server implements dConomyServer, dConomyUser {

    private final Server serv;
    private final BukkitdConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>> transactions;

    public Bukkit_Server(Server serv, BukkitdConomy dCo) {
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>>();
    }

    /** {@inheritDoc} */
    @Override
    public final dConomyUser getUser(String name) {
        Player player = serv.getPlayer(name);
        if (player != null) {
            return new Bukkit_User(player);
        }
        else {
            OfflinePlayer offplayer = serv.getOfflinePlayer(name);
            if (offplayer != null) {
                return new Bukkit_OfflineUser(offplayer);
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final Logger getServerLogger() {
        return dCo.getLogger();
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return "SERVER";
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isConsole() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void error(final String key, final Object... args) {
        getServerLogger().log(Level.FINE, MessageTranslator.translate(key, getUserLocale(), args));
    }

    /** {@inheritDoc} */
    @Override
    public void message(final String key, final Object... args) {
        getServerLogger().info(MessageTranslator.translate(key, getUserLocale(), args));
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void newTransaction(AccountTransaction transaction) {
        for (Class<? extends AccountTransactionEvent> clazz : transactions.keySet()) {
            //noinspection SuspiciousMethodCalls
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    //noinspection SuspiciousMethodCalls
                    AccountTransactionEvent event = clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Bukkit.getPluginManager().callEvent(event);
                    break;
                }
                catch (Exception ex) {
                    ((Logman) getServerLogger()).logStacktrace("Exception occured while calling AccountTransactionEvent", ex);
                }
            }
        }
    }

    /**
     * {@inheritDoc}<br>
     * clazz should be assignable from AccountTransactionEvent
     */
    @Override
    public void registerTransactionHandler(Class<? extends TransactionHookEvent> clazz, Class<? extends AccountTransaction> transaction) {
        if (AccountTransactionEvent.class.isAssignableFrom(clazz)) {
            transactions.putIfAbsent(clazz.asSubclass(AccountTransactionEvent.class), transaction);
        }
        else {
            throw new IllegalArgumentException("Class: " + clazz.getName() + " is not an instance of AccountTransactionEvent");
        }
    }

    /**
     * {@inheritDoc}<br>
     * clazz should be assignable from AccountTransactionEvent
     */
    @Override
    public void unregisterTransactionHandler(Class<? extends TransactionHookEvent> clazz) {
        //noinspection SuspiciousMethodCalls
        if (transactions.containsKey(clazz)) {
            //noinspection SuspiciousMethodCalls
            transactions.remove(clazz);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
