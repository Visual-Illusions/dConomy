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
package net.visualillusionsent.dconomy.api.account.wallet;

import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.InvalidPluginException;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.Set;
import java.util.UUID;

import static net.visualillusionsent.dconomy.api.account.wallet.WalletAction.*;

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
     * @param userUUID
     *         the {@link UUID} of the user
     * @param forceWallet
     *         {@code true} to create a new wallet; {@code false} otherwise
     *
     * @return wallet balance
     *
     * @throws AccountingException
     * @throws AccountNotFoundException
     *         if the account is non-existent
     */
    public static double walletBalance(UUID userUUID, boolean forceWallet) throws AccountingException, AccountNotFoundException {
        if (handler.verifyAccount(userUUID) || forceWallet) {
            return handler.getWalletByUUID(userUUID).getBalance();
        }
        throw new AccountNotFoundException("Wallet", userUUID);
    }

    /**
     * Checks if a wallet is locked out
     *
     * @param userUUID
     *         the name of the user
     *
     * @return {@code true} if locked; {@code false} if not
     *
     * @throws AccountNotFoundException
     */
    public static boolean isLocked(UUID userUUID) throws AccountNotFoundException {
        if (handler.verifyAccount(userUUID)) {
            return handler.getWalletByUUID(userUUID).isLocked();
        }
        throw new AccountNotFoundException("Wallet", userUUID);
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
        if (handler.verifyAccount(dCoUser.getUUID()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).deposit(deposit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_DEPOSIT, deposit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getUUID());
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
        if (handler.verifyAccount(dCoUser.getUUID()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).debit(debit);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_DEBIT, debit));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getUUID());
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
        try {
            return walletSet(addOn, dCoBase.getServer().getUser(userName), set, forceWallet);
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
        if (handler.verifyAccount(dCoUser.getUUID()) || forceWallet) {
            double newBalance = handler.getWallet(dCoUser).setBalance(set);
            dCoBase.getServer().newTransaction(new WalletTransaction(addOn, dCoUser, PLUGIN_SET, set));
            return newBalance;
        }
        throw new AccountNotFoundException("Wallet", dCoUser.getUUID());
    }

    /**
     * Tests a wallet debit before doing any modifications
     *
     * @param userUUID
     *         the {@link UUID} of the user who's wallet to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testWalletDebit(UUID userUUID, String debit) throws AccountingException {
        if (handler.verifyAccount(userUUID)) {
            handler.getWalletByUUID(userUUID).testDebit(debit);
        }
    }

    /**
     * Tests a wallet debit before doing any modifications
     *
     * @param userUUID
     *         the {@link UUID} of the user who's wallet to test
     * @param debit
     *         the amount to test debit
     *
     * @throws AccountingException
     */
    public static void testWalletDebit(UUID userUUID, double debit) throws AccountingException {
        if (handler.verifyAccount(userUUID)) {
            handler.getWalletByUUID(userUUID).testDebit(debit);
        }
    }

    /**
     * Tests a wallet deposit before doing any modifications
     *
     * @param userUUID
     *         the {@link UUID} of the user who's wallet to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testWalletDeposit(UUID userUUID, String deposit) throws AccountingException {
        if (handler.verifyAccount(userUUID)) {
            handler.getWalletByUUID(userUUID).testDeposit(deposit);
        }
    }

    /**
     * Tests a wallet deposit before doing any modifications
     *
     * @param userUUID
     *         the {@link UUID} of the user who's wallet to test
     * @param deposit
     *         the amount to test deposit
     *
     * @throws AccountingException
     */
    public static void testWalletDeposit(UUID userUUID, double deposit) throws AccountingException {
        if (handler.verifyAccount(userUUID)) {
            handler.getWalletByUUID(userUUID).testDeposit(deposit);
        }
    }

    /**
     * Gets the {@link UUID}(s) of known wallet owners
     *
     * @return set of owner {@link UUID}(s)
     */
    public static Set<UUID> getWalletOwners() {
        return handler.getWallets().keySet();
    }

    private static dConomyAddOn pluginNameToAddOn(String pluginName) throws InvalidPluginException {
        if (pluginName == null) {
            throw new InvalidPluginException("Plugin Name cannot be null");
        }
        return dCoBase.getServer().getPluginAsAddOn(pluginName);
    }
}
