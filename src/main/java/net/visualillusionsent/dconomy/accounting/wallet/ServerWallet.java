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

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.UUID;

/**
 * Server Wallet container
 *
 * @author Jason (darkdiplomat)
 */
public final class ServerWallet extends Wallet {

    private final boolean maxAlways;

    public ServerWallet(boolean maxAlways) {
        super(dConomyServer.SERVERUUID, maxAlways ? 999999999999999999D : dCoBase.getProperties().getDouble("server.balance"), dCoBase.getProperties().getBooleanValue("server.account.locked"), null);
        this.maxAlways = maxAlways;
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double getBalance() {
        return maxAlways ? 999999999999999999D : super.getBalance();
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double deposit(double add) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.deposit(add);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double deposit(String add) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.deposit(add);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double debit(double remove) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.debit(remove);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double debit(String remove) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.debit(remove);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double setBalance(double set) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.setBalance(set);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS
     */
    @Override
    public final double setBalance(String set) throws AccountingException {
        return maxAlways ? 999999999999999999D : super.setBalance(set);
    }

    /**
     * {@inheritDoc}<br>
     * Preforms checks on if the SERVER has MAX MONEY ALWAYS and stores it to the config
     */
    @Override
    protected final void save() {
        if (!maxAlways) {
            dCoBase.getProperties().setServerBalance(getBalance());
        }
        dCoBase.getProperties().setServerAccountLock(locked);
    }

    /** {@inheritDoc} */
    @Override
    public final boolean reload() {
        this.balance = dCoBase.getProperties().getDouble("server.balance");
        this.locked = dCoBase.getProperties().getBooleanValue("server.account.locked");
        return true;
    }
}
