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
package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import net.canarymod.Canary;
import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.Server;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;
import net.canarymod.logger.CanaryLevel;
import net.canarymod.logger.Logman;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.AccountTransactionHook;
import net.visualillusionsent.minecraft.server.mod.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.dconomy.accounting.AccountTransaction;
import net.visualillusionsent.minecraft.server.mod.interfaces.MineChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModServer;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;

/**
 * Canary Server wrapper for Mod_Server implementation
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public class Canary_Server implements IModServer, IModUser{

    private final Server serv;
    private final dConomy dCo;
    private final ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>> transactions;

    public Canary_Server(Server serv, dConomy dCo){
        this.serv = serv;
        this.dCo = dCo;
        this.transactions = new ConcurrentHashMap<Class<? extends Hook>, Class<? extends AccountTransaction>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final IModUser getUser(String name){
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

    /**
     * {@inheritDoc}
     */
    @Override
    public final Logger getServerLogger(){
        return dCo.getLogman();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ModType getModType(){
        return ModType.CANARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName(){
        return "SERVER";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String perm){
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isConsole(){
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void error(final String key, final Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().log(CanaryLevel.NOTICE, MineChatForm.removeFormating(key));
        }
        else {
            getServerLogger().log(CanaryLevel.NOTICE, MessageTranslator.transFormMessage(key, false, args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void message(final String key, final Object... args){
        if (args == null || key.trim().isEmpty()) {
            getServerLogger().info(MineChatForm.removeFormating(key));
        }
        else {
            getServerLogger().info(MessageTranslator.transFormMessage(key, false, args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void newTransaction(AccountTransaction transaction){
        for (Class<?> clazz : transactions.keySet()) {
            if (transaction.getClass().isAssignableFrom(transactions.get(clazz))) {
                try {
                    AccountTransactionHook hook = (AccountTransactionHook) clazz.getConstructor(transactions.get(clazz)).newInstance(transaction);
                    Canary.hooks().callHook(hook);
                    break;
                }
                catch (Exception ex) {
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
    public void registerTransactionHandler(Class<?> clazz, Class<? extends AccountTransaction> transaction){
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
    public void deregisterTransactionHandler(Class<?> clazz){
        if (transactions.containsKey(clazz)) {
            transactions.remove(clazz);
        }
    }
}
