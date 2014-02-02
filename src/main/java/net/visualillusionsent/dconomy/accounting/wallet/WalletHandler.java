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
package net.visualillusionsent.dconomy.accounting.wallet;

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.DataSourceType;
import net.visualillusionsent.dconomy.data.wallet.WalletDataSource;
import net.visualillusionsent.dconomy.data.wallet.WalletMySQLSource;
import net.visualillusionsent.dconomy.data.wallet.WalletSQLiteSource;
import net.visualillusionsent.dconomy.data.wallet.WalletXMLSource;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wallet Handler class<br>
 * manages Wallets
 *
 * @author Jason (darkdiplomat)
 */
public final class WalletHandler {

    private final ConcurrentHashMap<String, Wallet> wallets;
    private final ServerWallet servwallet;
    private final WalletDataSource source;
    private static boolean loaded;

    public WalletHandler(DataSourceType type) {
        if (loaded) {
            throw new PluginInitializationException("WalletHandler already running");
        }

        wallets = new ConcurrentHashMap<String, Wallet>();
        servwallet = new ServerWallet(dCoBase.getProperties().getBooleanValue("server.max.always"));
        if (type == DataSourceType.MYSQL) {
            source = new WalletMySQLSource(this);
        }
        else if (type == DataSourceType.SQLITE) {
            source = new WalletSQLiteSource(this);
        }
        else {
            source = new WalletXMLSource(this);
        }
        source.load();
        loaded = true;
    }

    /**
     * Gets a {@link Wallet} by a user's name
     *
     * @param username
     *         the user's name to get a wallet for
     *
     * @return the {@link Wallet} for the user, creating a new one if nessary
     */
    public final Wallet getWalletByName(String username) {
        if (username.equals("SERVER")) {
            return servwallet;
        }
        else if (verifyAccount(username)) {
            return wallets.get(username);
        }
        return newWallet(username);
    }

    /**
     * Gets a {@link Wallet} for a {@link net.visualillusionsent.dconomy.api.dConomyUser}
     *
     * @param user
     *         the {@link net.visualillusionsent.dconomy.api.dConomyUser} to get a wallet for
     *
     * @return the {@link Wallet} for the user if found; {@code null} if not found
     */
    public final Wallet getWallet(dConomyUser user) {
        return getWalletByName(user.getName());
    }

    /**
     * Adds a {@link Wallet} to the manager
     *
     * @param wallet
     *         the {@link Wallet} to be added
     */
    public final void addWallet(Wallet wallet) {
        wallets.put(wallet.getOwner(), wallet);
    }

    /**
     * Checks if a {@link Wallet} exists
     *
     * @param username
     *         the user's name to check Wallet for
     *
     * @return {@code true} if the wallet exists; {@code false} otherwise
     */
    public final boolean verifyAccount(String username) {
        return wallets.containsKey(username);
    }

    /**
     * Creates a new {@link Wallet} with default balance
     *
     * @param username
     *         the user's name to create a wallet for
     *
     * @return the new {@link Wallet}
     */
    public final Wallet newWallet(String username) {
        Wallet wallet = new UserWallet(username, dCoBase.getProperties().getDouble("default.balance"), false, source);
        addWallet(wallet);
        return wallet;
    }

    /**
     * Gets an unmodifiable map of all the wallets
     *
     * @return unmodifiable map of wallets
     */
    public final Map<String, Wallet> getWallets() {
        return Collections.unmodifiableMap(wallets);
    }

    /** Cleans up */
    public final void cleanUp() {
        wallets.clear();
        loaded = false;
    }
}
