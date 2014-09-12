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

import net.visualillusionsent.dconomy.accounting.Account;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.data.wallet.WalletDataSource;

import java.util.UUID;

/**
 * Wallet class
 *
 * @author Jason (darkdiplomat)
 */
public abstract class Wallet extends Account {

    protected boolean locked;

    public Wallet(UUID owner, double balance, boolean locked, WalletDataSource source) {
        super(owner, balance, source);
        this.locked = locked;
    }

    /**
     * Tests a debit before modifying the wallet
     *
     * @param remove
     *         the amount to test removal for
     *
     * @throws AccountingException
     *         if unable to debit the money
     */
    public final void testDebit(double remove) throws AccountingException {
        if (locked) {
            throw new AccountingException("error.account.lock", this.owner, "WALLET");
        }
        if (balance - remove < 0) {
            throw new AccountingException("error.no.money");
        }
    }

    /**
     * Tests a debit before modifying the wallet
     *
     * @param remove
     *         the amount to test removal for
     *
     * @throws AccountingException
     *         if unable to debit the money
     */
    public final void testDebit(String remove) throws AccountingException {
        testDebit(this.testArgumentString(remove));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void testDeposit(double add) throws AccountingException {
        if (locked) {
            throw new AccountingException("error.account.lock", this.owner, "WALLET");
        }
        super.testDeposit(add);
    }

    public final void setLockOut(boolean locked) {
        if (this.locked != locked) {
            this.locked = locked;
            this.save();
        }
    }

    public final boolean isLocked() {
        return locked;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Wallet) {
            return this == obj;
        }
        else if (obj instanceof String) {
            return this.owner.equals(obj);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        int hash = 7;
        hash = hash * 3 + owner.hashCode();
        hash = hash * 3 + super.hashCode();
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return String.format("Wallet[Owner: %s Balance: %.2f IsLocked: %b]", owner, balance, locked);
    }
}
