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
package net.visualillusionsent.dconomy.bukkit;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletBalanceEvent;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletDebitEvent;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletDepositEvent;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletSetBalanceEvent;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletTransactionEvent;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_DEBIT;
import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_DEPOSIT;
import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_SET;
import static org.bukkit.event.EventPriority.HIGHEST;

public final class BukkitdConomyAPIListener implements Listener {

    BukkitdConomyAPIListener(BukkitdConomy plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = HIGHEST)
    public final void walletBalance(WalletBalanceEvent event) {
        try {
            if (WalletHandler.verifyAccount(event.getUserName())) {
                event.setBalance(WalletHandler.getWalletByName(event.getUserName()).getBalance());
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void walletDeposit(WalletDepositEvent event) {
        try {
            if (WalletHandler.verifyAccount(event.getUserName())) {
                WalletHandler.getWalletByName(event.getUserName()).deposit(event.getDeposit());
                Bukkit.getPluginManager().callEvent(new WalletTransactionEvent(new WalletTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_DEPOSIT, event.getDeposit())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void walletDebit(WalletDebitEvent event) {
        try {
            if (WalletHandler.verifyAccount(event.getUserName())) {
                WalletHandler.getWalletByName(event.getUserName()).debit(event.getDebit());
                Bukkit.getPluginManager().callEvent(new WalletTransactionEvent(new WalletTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_DEBIT, event.getDebit())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Event: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void walletSet(WalletSetBalanceEvent event) {
        try {
            if (WalletHandler.verifyAccount(event.getUserName())) {
                WalletHandler.getWalletByName(event.getUserName()).setBalance(event.getToSet());
                Bukkit.getPluginManager().callEvent(new WalletTransactionEvent(new WalletTransaction(event.getCaller(), dCoBase.getServer().getUser(event.getUserName()), PLUGIN_SET, event.getToSet())));
            }
            else {
                event.setErrorMessage("Wallet Not Found");
            }
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getCaller().getName() + "'. Reason: " + aex.getMessage());
            event.setErrorMessage(aex.getMessage());
        }
    }

    @EventHandler(priority = HIGHEST)
    public final void debugTransaction(WalletTransactionEvent event) {
        dCoBase.debug("WalletTransactionEvent called: " + event.getTransaction());
    }
}
