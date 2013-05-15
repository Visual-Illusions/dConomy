package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.dCoDataSource;

public abstract class Account{

    protected final String owner;
    protected double balance;
    private final dCoDataSource datasource;

    public Account(String owner, double balance, dCoDataSource datasource){
        if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
        }
        this.owner = owner;
        this.balance = balance;
        this.datasource = datasource;
    }

    public String getOwner(){
        return owner;
    }

    public double getBalance(){
        return balance;
    }

    public double deposit(double add) throws AccountingException{
        balance += testArgumentDouble(add);
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    public double deposit(String add) throws AccountingException{
        return deposit(testArgumentString(add));
    }

    public double debit(double remove) throws AccountingException{
        double toRemove = testArgumentDouble(remove);
        balance -= toRemove;
        double max = dCoBase.getProperties().getDouble("max.account.balance");
        if (balance > max) {
            balance = max;
        }
        this.save();
        return balance;
    }

    public double debit(String remove) throws AccountingException{
        return debit(testArgumentString(remove));
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

    public final dCoDataSource getDataSource(){
        return datasource;
    }
}
