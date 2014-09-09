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
package net.visualillusionsent.dconomy.accounting;

import net.visualillusionsent.dconomy.accounting.wallet.ServerWallet;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.dCoDataSource;

import java.util.UUID;

/**
 * Account base class
 *
 * @author Jason (darkdiplomat)
 */
public abstract class Account {

    protected final UUID owner;
    protected double balance;
    protected final dCoDataSource datasource;

    /**
     * Constructs a new Account
     *
     * @param owner
     *         the owner of the account
     * @param balance
     *         the balance of the account
     * @param datasource
     *         the {@link dCoDataSource} instance used to save/load the Account
     */
    public Account(UUID owner, double balance, dCoDataSource datasource) {
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.owner = owner;
        this.balance = balance;
        this.datasource = datasource;
    }

    /**
     * Gets the owner of the Account
     *
     * @return Owner's name
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Gets the Account's balance
     *
     * @return account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Deposits money into the account
     *
     * @param add
     *         the amount to be deposited
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double deposit(double add) throws AccountingException {
        balance += testArgumentDouble(add);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    /**
     * Deposits money into the account
     *
     * @param add
     *         the amount to be deposited
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double deposit(String add) throws AccountingException {
        return deposit(testArgumentString(add));
    }

    /**
     * Tests a deposit before adding it.
     *
     * @param add
     *         the amount to be added
     *
     * @throws AccountingException
     *         should an AccountingException occur
     */
    public void testDeposit(String add) throws AccountingException {
        testDeposit(testArgumentString(add));
    }

    /**
     * Tests a deposit before adding it.
     *
     * @param add
     *         the amount to be added
     *
     * @throws AccountingException
     *         should an AccountingException occur
     */
    public void testDeposit(double add) throws AccountingException {
        double balance = this.balance + testArgumentDouble(add);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            if (!this.owner.equals(ServerWallet.SERVERUUID)) {
                throw new AccountingException("error.money.max", this.owner, this.getClass().getSimpleName().toUpperCase());
            }
        }
    }

    /**
     * Debits money from an account
     *
     * @param remove
     *         the amount to be debited
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double debit(double remove) throws AccountingException {
        double toRemove = testArgumentDouble(remove);
        balance -= toRemove;
        this.save();
        return balance;
    }

    /**
     * Debits money from an account
     *
     * @param remove
     *         the amount to be debited
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double debit(String remove) throws AccountingException {
        return debit(testArgumentString(remove));
    }

    /**
     * Sets an Account balance
     *
     * @param set
     *         the amount the balance should be set to
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double setBalance(double set) throws AccountingException {
        balance = testArgumentDouble(set);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    /**
     * Sets an Account balance
     *
     * @param set
     *         the amount the balance should be set to
     *
     * @return the new account balance
     *
     * @throws AccountingException
     *         if an Accounting Exception occurs
     */
    public double setBalance(String set) throws AccountingException {
        return setBalance(testArgumentString(set));
    }

    /**
     * Tests a String for a double value
     *
     * @param value
     *         the value to test
     *
     * @return double value of the String
     *
     * @throws AccountingException
     */
    protected final double testArgumentString(String value) throws AccountingException {
        try {
            return Double.parseDouble(value);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
    }

    /**
     * Tests a double for negative value and rounds it to 2 decimal places
     *
     * @param value
     *         the value to test
     *
     * @return the dConomy rounded value
     *
     * @throws AccountingException
     */
    protected final double testArgumentDouble(double value) throws AccountingException {
        if (value < 0) {
            throw new AccountingException("error.min");
        }
        return Math.round(value * 100) / 100.0f; // Even out the number to a 0.## form
    }

    /** Override and insert Account saving code here */
    protected abstract void save();

    /** Override and insert Account reload code here */
    public abstract boolean reload();

    /**
     * Gets the {@link dCoDataSource} associated with the Account
     *
     * @return the {@link dCoDataSource}
     */
    public final dCoDataSource getDataSource() {
        return datasource;
    }
}
