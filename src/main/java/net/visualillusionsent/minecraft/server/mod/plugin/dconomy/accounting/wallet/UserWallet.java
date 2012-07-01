package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public final class UserWallet extends Wallet{

    public UserWallet(String owner, double balance){
        super(owner, balance);
        WalletHandler.addWallet(this);
    }

    protected final void save(){
        dCoBase.getDataHandler().addToQueue(this);
    }
}
