package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletDataSource;

public final class UserWallet extends Wallet{

    public UserWallet(String owner, double balance, WalletDataSource source){
        super(owner, balance, source);
        WalletHandler.addWallet(this);
    }

    protected final void save(){
        dCoBase.getDataHandler().addToQueue(this);
    }
}
