package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletDataSource;

public final class UserWallet extends Wallet{

    public UserWallet(String owner, double balance, WalletDataSource source){
        super(owner, balance, source);
        WalletHandler.addWallet(this);
    }

    @Override
    public final double debit(double remove){
        testDebit(remove);
        return super.debit(remove);
    }

    @Override
    public final double debit(String remove){
        return testDebit(testArgumentString(remove)) ? super.debit(remove) : getBalance();
    }

    private final boolean testDebit(double remove){
        if (balance - remove < 0) {
            throw new AccountingException("error.no.money");
        }
        return true;
    }

    protected final void save(){
        dCoBase.getDataHandler().addToQueue(this);
    }
}
