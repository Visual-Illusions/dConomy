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

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.wallet.WalletDataSource;

import java.util.UUID;

/**
 * User Wallet container
 *
 * @author Jason (darkdiplomat)
 */
public final class UserWallet extends Wallet {

    /**
     * Constructs a new UserWallet and adds it to the {@link WalletHandler}
     *
     * @param owner
     *         the owner's name
     * @param balance
     *         the current balance
     * @param locked
     *         whether or not the Wallet is locked out
     * @param source
     *         the {@link WalletDataSource} used to store the Wallet
     */
    public UserWallet(UUID owner, double balance, boolean locked, WalletDataSource source) {
        super(owner, balance, locked, source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void save() {
        dCoBase.getDataHandler().addToQueue(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reload() {
        return datasource.reloadAccount(this);
    }
}
