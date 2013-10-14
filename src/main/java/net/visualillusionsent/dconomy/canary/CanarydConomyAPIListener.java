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
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.canary.api.*;
import net.visualillusionsent.dconomy.dCoBase;

public final class CanarydConomyAPIListener implements PluginListener {

    CanarydConomyAPIListener(CanarydConomy dCo) {
        Canary.hooks().registerListener(this, dCo);
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletBalance(WalletBalanceHook hook) {
        try {
            if (WalletHandler.verifyAccount(hook.getUserName())) {
                hook.setBalance(WalletHandler.getWalletByName(hook.getUserName()).getBalance());
            } else {
                hook.setErrorMessage("Wallet Not Found");
            }
        } catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletDeposit(final WalletDepositHook hook) {
        try {
            if (WalletHandler.verifyAccount(hook.getUserName())) {
                WalletHandler.getWalletByName(hook.getUserName()).deposit(hook.getDeposit());
                Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), WalletTransaction.ActionType.PLUGIN_DEPOSIT, hook.getDeposit())));
            } else {
                hook.setErrorMessage("Wallet Not Found");
            }
        } catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletDebit(WalletDebitHook hook) {
        try {
            if (WalletHandler.verifyAccount(hook.getUserName())) {
                WalletHandler.getWalletByName(hook.getUserName()).debit(hook.getDebit());
                Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), WalletTransaction.ActionType.PLUGIN_DEBIT, hook.getDebit())));
            } else {
                hook.setErrorMessage("Wallet Not Found");
            }
        } catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletSet(WalletSetBalanceHook hook) {
        try {
            if (WalletHandler.verifyAccount(hook.getUserName())) {
                WalletHandler.getWalletByName(hook.getUserName()).setBalance(hook.getToSet());
                Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getCaller(), dCoBase.getServer().getUser(hook.getUserName()), WalletTransaction.ActionType.PLUGIN_SET, hook.getToSet())));
            } else {
                hook.setErrorMessage("Wallet Not Found");
            }
        } catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getCaller().getName() + "'. Reason: " + aex.getMessage());
            hook.setErrorMessage(aex.getMessage());
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void debugTransaction(WalletTransactionHook hook) {
        dCoBase.debug("WalletTransactionHook called");
    }
}