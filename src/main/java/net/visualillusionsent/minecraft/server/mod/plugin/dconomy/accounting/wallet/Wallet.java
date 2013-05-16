package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletDataSource;

public abstract class Wallet extends Account{

    public Wallet(String owner, double balance, WalletDataSource source){
        super(owner, balance, source);
    }

    @Override
    public final boolean equals(Object obj){
        if (obj instanceof Wallet) {
            return this == obj;
        }
        else if (obj instanceof String) {
            return this.owner.equals(obj);
        }
        return false;
    }

    public final void testDebit(double remove){
        if (balance - remove < 0) {
            throw new AccountingException("error.no.money");
        }
    }

    public final void testDebit(String remove){
        testDebit(this.testArgumentString(remove));
    }

    @Override
    public final int hashCode(){
        int hash = 7;
        hash = hash * 3 + owner.hashCode();
        hash = hash * 3 + super.hashCode();
        return hash;
    }

    @Override
    public final String toString(){
        return String.format("Wallet[Owner: %s Balance: %.2f]", owner, balance);
    }
}
