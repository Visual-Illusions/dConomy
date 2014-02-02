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
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.TransactionHookEvent;
import net.visualillusionsent.dconomy.api.account.AccountTransaction;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.CanaryConomy;
import net.visualillusionsent.dconomy.canary.api.account.AccountTransactionHook;
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
    private final CanaryConomy dCo;
    private final ConcurrentHashMap<Class<? extends AccountTransactionHook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, CanaryConomy dCo) {
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

    @Override
    public final String[] getUserNames() {
        return Canary.getServer().getKnownPlayerNames();
    }

    /** {@inheritDoc} */
    @Override
    public final Logger getServerLogger() {
        //return dCo.getLogman();
        return null;
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
        Canary.getServer().message(message);
    }

    /** {@inheritDoc} */
    @Override
    public void message(String message) {
        Canary.getServer().notice(message);
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
                    //getServerLogger().error("Exception occurred while calling AccountTransactionHook", ex);
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

    @Override
    public dConomyAddOn getPluginAsAddOn(String pluginName) throws InvalidPluginException {
        Plugin plugin = Canary.loader().getPlugin(pluginName);
        if (plugin == null) {
            throw new InvalidPluginException("Plugin not found");
        }
        return new Canary_Plugin(plugin);
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
