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
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.Set;

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
    private static WalletHandler handler;

    public static void setHandler(WalletHandler whandler) {
        if (handler == null) {
            handler = whandler;
        }
    }

    /**
     * Gets the balance of a user's Wallet
     *
     * @param userName
     *         the name of the user
     * @param forceWallet
     *         {@code true} to create a new wallet; {@code false} otherwise
     *
     * @return wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     *         if the account is non-existant
     */
    public static double walletBalance(String userName, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(userName) || forceWallet) {
            return handler.getWalletByName(userName).getBalance();
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    /**
     * Checks if a wallet is locked out
     *
     * @param userName
     *         the name of the user
     *
     * @return {@code true} if locked; {@code false} if not
     *
     * @throws AccountNotFoundException
     */
    public static boolean isLocked(String userName) throws AccountNotFoundException {
        if (handler.verifyAccount(userName)) {
            return handler.getWalletByName(userName).isLocked();
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    /**
     * Deposits money into a user's wallet
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param deposit
     *         the amount to deposit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDeposit(String pluginName, String userName, String deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        try {
            return walletDeposit(pluginNameToAddOn(pluginName), dCoBase.getServer().getUser(userName), Double.valueOf(deposit), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Deposits money into a user's wallet
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param deposit
     *         the amount to deposit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDeposit(String pluginName, String userName, double deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return walletDeposit(pluginNameToAddOn(pluginName), dCoBase.getServer().getUser(userName), deposit, forceWallet);
    }

    /**
     * Deposits money into a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param deposit
     *         the amount to deposit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDeposit(dConomyAddOn addOn, String userName, double deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        return walletDeposit(addOn, dCoBase.getServer().getUser(userName), deposit, forceWallet);
    }

    /**
     * Deposits money into a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param deposit
     *         the amount to deposit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDeposit(dConomyAddOn addOn, dConomyUser dCoUser, String deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        try {
            return walletDeposit(addOn, dCoUser, Double.valueOf(deposit), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Deposits money into a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param deposit
     *         the amount to deposit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDeposit(dConomyAddOn addOn, dConomyUser dCoUser, double deposit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(dCoUser.getName()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).deposit(deposit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_DEPOSIT, deposit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getName());
    }

    /**
     * Debits money from a user's wallet
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param debit
     *         the amount to debit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDebit(String pluginName, String userName, String debit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        try {
            return walletDebit(pluginNameToAddOn(pluginName), dCoBase.getServer().getUser(userName), Double.valueOf(debit), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Debits money from a user's wallet
     *
     * @param pluginName
     *         the name of the plugin making the deposit
     * @param userName
     *         the name of the user
     * @param debit
     *         the amount to debit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDebit(String pluginName, String userName, double debit, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return walletDebit(pluginNameToAddOn(pluginName), dCoBase.getServer().getUser(userName), debit, forceWallet);
    }

    /**
     * Debits money from a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param debit
     *         the amount to debit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDebit(dConomyAddOn addOn, String userName, double debit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        return walletDebit(addOn, dCoBase.getServer().getUser(userName), debit, forceWallet);
    }

    /**
     * Debits money from a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param debit
     *         the amount to debit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDebit(dConomyAddOn addOn, dConomyUser dCoUser, String debit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        try {
            return walletDebit(addOn, dCoUser, Double.valueOf(debit), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Debits money from a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param debit
     *         the amount to debit
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return the new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletDebit(dConomyAddOn addOn, dConomyUser dCoUser, double debit, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(dCoUser.getName()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).debit(debit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_DEBIT, debit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getName());
    }

    /**
     * Sets the balance of a user's wallet
     *
     * @param pluginName
     *         the name of the plugin doing the setting
     * @param userName
     *         the name of the user
     * @param set
     *         the amount to be set
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletSet(String pluginName, String userName, String set, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        try {
            return walletSet(pluginNameToAddOn(pluginName), dCoBase.getServer().getUser(userName), Double.valueOf(set), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Sets the balance of a user's wallet
     *
     * @param pluginName
     *         the name of the plugin doing the setting
     * @param userName
     *         the name of the user
     * @param set
     *         the amount to be set
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletSet(String pluginName, String userName, double set, boolean forceWallet) throws AccountingException, AccountNotFoundException, InvalidPluginException {
        return walletSet(pluginNameToAddOn(pluginName), userName, set, forceWallet);
    }

    /**
     * Sets the balance of a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param userName
     *         the name of the user
     * @param set
     *         the amount to be set
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletSet(dConomyAddOn addOn, String userName, double set, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(userName) || forceWallet) {
            double newBalance = handler.getWalletByName(userName).setBalance(set);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoBase.getServer().getUser(userName), PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", userName);
    }

    /**
     * Sets the balance of a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param set
     *         the amount to be set
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletSet(dConomyAddOn addOn, dConomyUser dCoUser, String set, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        try {
            return walletSet(addOn, dCoUser, Double.valueOf(set), forceWallet);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Sets the balance of a user's wallet
     *
     * @param addOn
     *         the dConomyAddOn instance
     * @param dCoUser
     *         the user
     * @param set
     *         the amount to be set
     * @param forceWallet
     *         {@code true} to force a new wallet; {@code false} otherwise
     *
     * @return new wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     * @throws InvalidPluginException
     */
    public static double walletSet(dConomyAddOn addOn, dConomyUser dCoUser, double set, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(dCoUser.getName()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).setBalance(set);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getName());
    }

    /**
     * Tests a wallet debit before doing any modifications
     *
     * @param userName
     *         the name of the user who's wallet to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testWalletDebit(String userName, String debit) throws AccountingException {
        if (handler.verifyAccount(userName)) {
            handler.getWalletByName(userName).testDebit(debit);
        }
    }

    /**
     * Tests a wallet debit before doing any modifications
     *
     * @param userName
     *         the name of the user who's wallet to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testWalletDebit(String userName, double debit) throws AccountingException {
        if (handler.verifyAccount(userName)) {
            handler.getWalletByName(userName).testDebit(debit);
        }
    }

    /**
     * Tests a wallet deposit before doing any modifications
     *
     * @param userName
     *         the name of the user who's wallet to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testWalletDeposit(String userName, String deposit) throws AccountingException {
        if (handler.verifyAccount(userName)) {
            handler.getWalletByName(userName).testDeposit(deposit);
        }
    }

    /**
     * Tests a wallet deposit before doing any modifications
     *
     * @param userName
     *         the name of the user who's wallet to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testWalletDeposit(String userName, double deposit) throws AccountingException {
        if (handler.verifyAccount(userName)) {
            handler.getWalletByName(userName).testDeposit(deposit);
        }
    }

    /**
     * Gets the names of known wallet owners
     *
     * @return set of owner names
     */
    public static Set<String> getWalletOwners() {
        return handler.getWallets().keySet();
    }

    private static dConomyAddOn pluginNameToAddOn(String pluginName) throws InvalidPluginException {
        if (pluginName == null) {
            throw new InvalidPluginException("Plugin Name cannot be null");
        }
        return dCoBase.getServer().getPluginAsAddOn(pluginName);
    }
}
