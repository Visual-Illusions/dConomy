/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.dconomy.accounting;

import net.visualillusionsent.minecraft.server.mod.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.dconomy.data.dCoDataSource;

/**
 * Account base class
 * 
 * @author Jason (darkdiplomat)
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class Account{

    protected final String owner;
    protected double balance;
    protected final dCoDataSource datasource;

    /**
     * Constructs a new Account
     * 
     * @param owner
     *            the owner of the account
     * @param balance
     *            the balance of the account
     * @param datasource
     *            the {@link dCoDataSource} instance used to save/load the Account
     */
    public Account(String owner, double balance, dCoDataSource datasource){
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
    public String getOwner(){
        return owner;
    }

    /**
     * Gets the Account's balance
     * 
     * @return account balance
     */
    public double getBalance(){
        return balance;
    }

    /**
     * Deposits money into the account
     * 
     * @param add
     *            the amount to be deposited
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double deposit(double add) throws AccountingException{
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
     *            the amount to be deposited
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double deposit(String add) throws AccountingException{
        return deposit(testArgumentString(add));
    }

    /**
     * Tests a deposit before adding it.
     * 
     * @param add
     *            the amount to be added
     * @throws AccountingException
     *             should an AccountingException occur
     */
    public void testDeposit(String add) throws AccountingException{
        testDeposit(testArgumentString(add));
    }

    /**
     * Tests a deposit before adding it.
     * 
     * @param add
     *            the amount to be added
     * @throws AccountingException
     *             should an AccountingException occur
     */
    public void testDeposit(double add) throws AccountingException{
        double balance = this.balance + testArgumentDouble(add);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            if (!this.owner.equals("SERVER")) {
                throw new AccountingException(MessageTranslator.transFormMessage("error.money.max", true, this.owner, this.getClass().getSimpleName().toUpperCase()));
            }
        }
    }

    /**
     * Debits money from an account
     * 
     * @param remove
     *            the amount to be debitted
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double debit(double remove) throws AccountingException{
        double toRemove = testArgumentDouble(remove);
        balance -= toRemove;
        this.save();
        return balance;
    }

    /**
     * Debits money from an account
     * 
     * @param remove
     *            the amount to be debitted
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double debit(String remove) throws AccountingException{
        return debit(testArgumentString(remove));
    }

    /**
     * Sets an Account balance
     * 
     * @param set
     *            the amount the balance should be set to
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double setBalance(double set) throws AccountingException{
        double toSet = testArgumentDouble(set);
        balance = toSet;
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
     *            the amount the balance should be set to
     * @return the new account balance
     * @throws AccountingException
     *             if an Accounting Exception occurs
     */
    public double setBalance(String set) throws AccountingException{
        return setBalance(testArgumentString(set));
    }

    /**
     * Tests a String for a double value
     * 
     * @param value
     *            the value to test
     * @return double value of the String
     * @throws AccountingException
     */
    protected final double testArgumentString(String value) throws AccountingException{
        double testNum = 0;
        try {
            testNum = Double.parseDouble(value);
        }
        catch (NumberFormatException nfe) {
            throw new AccountingException("error.nan");
        }
        return testNum;
    }

    /**
     * Tests a double for negative value and rounds it to 2 decimal places
     * 
     * @param value
     *            the value to test
     * @return the dConomy rounded value
     * @throws AccountingException
     */
    protected final double testArgumentDouble(double value) throws AccountingException{
        if (value < 0) {
            throw new AccountingException("error.min");
        }
        return Math.round(value * 100) / 100.0f; // Even out the number to a 0.## form
    }

    /**
     * Override and insert Account saving code here
     */
    protected abstract void save();

    /**
     * Override and insert Account reload code here
     */
    public abstract boolean reload();

    /**
     * Gets the {@link dCoDataSource} associated with the Account
     * 
     * @return the {@link dCoDataSource}
     */
    public final dCoDataSource getDataSource(){
        return datasource;
    }
}
