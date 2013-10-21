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
package net.visualillusionsent.dconomy.api.account.wallet;

import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.Collection;

import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_DEBIT;
import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_DEPOSIT;
import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.PLUGIN_SET;

/**
 * Wallet API Listener
 * <p/>
 * Plugins should use this API for Wallet actions<br/>
 * This is automatically call the CallBacks for extensions to track changes<br/>
 * <p/>
 * dConomyAddOn will be your plugin, wrapped in either CanaryPlugin or BukkitPlugin classes<br/>
 * ie: new CanaryPlugin(plugin)
 *
 * @author Jason (darkdiplomat)
 */
public final class WalletAPIListener {

    public static double getBalance(String userName, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (WalletHandler.verifyAccount(userName) || forceWallet) {
            return WalletHandler.getWalletByName(userName).getBalance();
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    public static boolean isLocked(String userName) throws AccountNotFoundException {
        if (WalletHandler.verifyAccount(userName)) {
            return WalletHandler.getWalletByName(userName).isLocked();
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    public static double walletDeposit(String pluginName, String userName, double deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        dConomyAddOn addOn = pluginNameToAddOn(pluginName);
        if (WalletHandler.verifyAccount(userName) || forceWallet) {
            double newBalance = WalletHandler.getWalletByName(userName).deposit(deposit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_DEPOSIT, deposit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    public static double walletDebit(String pluginName, String userName, double debit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        dConomyAddOn addOn = pluginNameToAddOn(pluginName);
        if (WalletHandler.verifyAccount(userName) || forceWallet) {
            double newBalance = WalletHandler.getWalletByName(userName).debit(debit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_DEBIT, debit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    public static double walletSet(String pluginName, String userName, double set, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        dConomyAddOn addOn = pluginNameToAddOn(pluginName);
        if (WalletHandler.verifyAccount(userName) || forceWallet) {
            double newBalance = WalletHandler.getWalletByName(userName).setBalance(set);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    public static void testWalletDebit(String userName, double debit) throws AccountingException {
        if (WalletHandler.verifyAccount(userName)) {
            WalletHandler.getWalletByName(userName).testDebit(debit);
        }
    }

    public static void testWalletDeposit(String userName, double debit) throws AccountingException {
        if (WalletHandler.verifyAccount(userName)) {
            WalletHandler.getWalletByName(userName).testDeposit(debit);
        }
    }

    public static Collection<Wallet> getWallets() {
        return WalletHandler.getWallets().values();
    }

    private static dConomyAddOn pluginNameToAddOn(String pluginName) throws InvalidPluginException {
        if (pluginName == null) {
            throw new InvalidPluginException("Plugin Name cannot be null");
        }
        return dCoBase.getServer().getPluginAsAddOn(pluginName);
    }
}
