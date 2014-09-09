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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wallet Handler class<br>
 * manages Wallets
 *
 * @author Jason (darkdiplomat)
 */
public final class WalletHandler {

    private final ConcurrentHashMap<UUID, Wallet> wallets;
    private final ServerWallet servwallet;
    private final WalletDataSource source;
    private static boolean loaded;

    public WalletHandler(DataSourceType type) {
        if (loaded) {
            throw new PluginInitializationException("WalletHandler already running");
        }

        wallets = new ConcurrentHashMap<UUID, Wallet>();
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
     * Gets a {@link Wallet} by a user's {@link UUID}
     *
     * @param userUUID
     *         the user's UUID to get a wallet for
     *
     * @return the {@link Wallet} for the user, creating a new one if necessary
     */
    public final Wallet getWalletByUUID(UUID userUUID) {
        if (userUUID.equals(ServerWallet.SERVERUUID)) {
            return servwallet;
        }
        else if (verifyAccount(userUUID)) {
            return wallets.get(userUUID);
        }
        return newWallet(userUUID);
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
        return getWalletByUUID(user.getUUID());
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
     * @param uuid
     *         the user's {@link UUID} to check Wallet for
     *
     * @return {@code true} if the wallet exists; {@code false} otherwise
     */
    public final boolean verifyAccount(UUID uuid) {
        return wallets.containsKey(uuid);
    }

    /**
     * Creates a new {@link Wallet} with default balance
     *
     * @param uuid
     *         the user's {@link UUID} to create a wallet for
     *
     * @return the new {@link Wallet}
     */
    public final Wallet newWallet(UUID uuid) {
        Wallet wallet = new UserWallet(uuid, dCoBase.getProperties().getDouble("default.balance"), false, source);
        addWallet(wallet);
        return wallet;
    }

    /**
     * Gets an unmodifiable map of all the wallets
     *
     * @return unmodifiable map of wallets
     */
    public final Map<UUID, Wallet> getWallets() {
        return Collections.unmodifiableMap(wallets);
    }

    /** Cleans up */
    public final void cleanUp() {
        wallets.clear();
        loaded = false;
    }
}
