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
package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.canarymod.Canary;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletBalanceEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletDebitEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletDepositEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletSetBalanceEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletTransactionEvent;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletTransactionHook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class dConomyBukkitAPIListener implements Listener{

    dConomyBukkitAPIListener(dConomy plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletBalance(WalletBalanceEvent event){
        try {
            event.setBalance(WalletHandler.getWallet(event.getUser()).getBalance());
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getRequester().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletDeposit(WalletDepositEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getDeposit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEPOSIT, event.getDeposit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletDebit(WalletDebitEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getDebit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEBIT, event.getDebit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletSet(WalletSetBalanceEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getToSet());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_SET, event.getToSet())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void debugTransaction(WalletTransactionEvent event){
        dCoBase.debug("WalletTransactionEvent called");
    }
}
