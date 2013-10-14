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
import net.canarymod.hook.Hook;
import net.canarymod.logger.CanaryLevel;
import net.canarymod.logger.Logman;
import net.visualillusionsent.dconomy.MessageTranslator;
import net.visualillusionsent.dconomy.accounting.AccountTransaction;
import net.visualillusionsent.dconomy.canary.CanarydConomy;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.MineChatForm;
import net.visualillusionsent.dconomy.modinterface.ModServer;
import net.visualillusionsent.dconomy.modinterface.ModType;
import net.visualillusionsent.dconomy.modinterface.ModUser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Canary Server wrapper for Mod_Server implementation
 *
 * @author Jason (darkdiplomat)
 */
public class Canary_Server implements ModServer, ModUser {

    private final Server serv;
    private final CanarydConomy dCo;
    private final ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, CanarydConomy dCo) {
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ModUser getUser(String name) {
        Player player = serv.matchPlayer(name);
        if (player != null) {
            return new Canary_User(player);
        } else {
            OfflinePlayer offplayer = serv.getOfflinePlayer(name);
            if (offplayer != null) {
                return new Canary_OfflineUser(offplayer);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Logger getServerLogger() {
        return dCo.getLogman();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ModType getModType() {
        return ModType.CANARY;
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
    public boolean hasPermission(String perm) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isConsole() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String key, final Object... args) {
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().log(CanaryLevel.NOTICE, MineChatForm.removeFormating(key));
        } else {
            getServerLogger().log(CanaryLevel.NOTICE, MessageTranslator.translate(key, getUserLocale(), args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(final String key, final Object... args) {
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().info(MineChatForm.removeFormating(key));
        } else {
            getServerLogger().info(MessageTranslator.translate(key, getUserLocale(), args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newTransaction(AccountTransaction transaction) {
        for (Class<?> clazz : transactions.keySet()) {
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionHook hook = (AccountTransactionHook) clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Canary.hooks().callHook(hook);
                    break;
                } catch (Exception ex) {
                    ((Logman) getServerLogger()).logStacktrace("Exception occured while calling AccountTransactionHook", ex);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void registerTransactionHandler(Class<?> clazz, Class<? extends AccountTransaction> transaction) {
        if (AccountTransactionHook.class.isAssignableFrom(clazz)) {
            if (!transactions.containsKey(clazz)) {
                transactions.put((Class<? extends AccountTransactionHook>) clazz, transaction);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deregisterTransactionHandler(Class<?> clazz) {
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
