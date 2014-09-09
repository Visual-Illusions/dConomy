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
package net.visualillusionsent.dconomy.bukkit.api;

import net.visualillusionsent.dconomy.api.TransactionHookEvent;
import net.visualillusionsent.dconomy.api.account.AccountTransaction;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.bukkit.BukkitConomy;
import net.visualillusionsent.dconomy.bukkit.api.account.AccountTransactionEvent;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
    private final BukkitConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>> transactions;

    public Bukkit_Server(Server serv, BukkitConomy dCo) {
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends AccountTransactionEvent>, Class<? extends AccountTransaction>>();
    }

    /** {@inheritDoc} */
    @Override
    public final dConomyUser getUser(String name) {
        if (name.equals("SERVER")) {
            return this;
        }

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

    @Override
    public final String[] getUserNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (Player player : serv.getOnlinePlayers()) {
            names.add(player.getName());
        }
        for (OfflinePlayer off : serv.getOfflinePlayers()) {
            names.add(off.getName());
        }
        return names.toArray(new String[names.size()]);
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
    public void error(String message) {
        getServerLogger().log(Level.WARNING, message);
    }

    /** {@inheritDoc} */
    @Override
    public void message(String message) {
        getServerLogger().log(Level.INFO, message);
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
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionEvent event = clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Bukkit.getPluginManager().callEvent(event);
                    break;
                }
                catch (Exception ex) {
                    getServerLogger().log(Level.WARNING, "Exception occured while calling AccountTransactionEvent", ex);
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
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }

    @Override
    public Bukkit_Plugin getPluginAsAddOn(String pluginName) {
        return new Bukkit_Plugin(Bukkit.getPluginManager().getPlugin(pluginName));
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
