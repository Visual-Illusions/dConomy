package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

/**
 * Wallet Transaction Hook <br>
 * Called when a Wallet balance changes
 * 
 * @author Jason (darkdiplomat)
 */
public final class WalletTransactionHook extends AccountTransactionHook{

    public WalletTransactionHook(WalletTransaction action){
        super(action);
    }

    public final WalletTransaction getTransaction(){
        return (WalletTransaction) action;
    }

}
