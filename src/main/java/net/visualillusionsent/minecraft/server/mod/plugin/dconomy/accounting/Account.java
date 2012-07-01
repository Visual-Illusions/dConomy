package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public abstract class Account{

    protected final String owner;
    protected double balance;

    public Account(String owner, double balance){
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.owner = owner;
        this.balance = balance;
    }

    public String getOwner(){
        return owner;
    }

    public double getBalance(){
        return balance;
    }

    public double addToBalance(double add) throws AccountingException{
        balance += testArgumentDouble(add);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    public double addToBalance(String add) throws AccountingException{
        return addToBalance(testArgumentString(add));
    }

    public double removeFromBalance(double remove) throws AccountingException{
        double toRemove = testArgumentDouble(remove);
        if (balance - toRemove < 0) {
            throw new AccountingException("error.no.money");
        }
        balance -= toRemove;
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    public double removeFromBalance(String remove) throws AccountingException{
        return removeFromBalance(testArgumentString(remove));
    }

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

    public double setBalance(String set) throws AccountingException{
        return setBalance(testArgumentString(set));
    }

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

    protected final double testArgumentDouble(double value) throws AccountingException{
        if (value < 0) {
            throw new AccountingException("error.min");
        }
        return Math.round(value * 100) / 100.0f; // Even out the number to a 0.## form
    }

    protected abstract void save();
}
