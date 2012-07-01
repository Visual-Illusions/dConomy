package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.AccountTransactionHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletAddBalanceHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletRemoveBalanceHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletSetBalanceHook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

public final class dConomyCanaryAPIListener implements PluginListener{

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletAdd(WalletAddBalanceHook hook){
        WalletHandler.getWallet(hook.getRecipient()).addToBalance(hook.getToAdd());
        Canary.hooks().callHook(new AccountTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_ADD, hook.getToAdd())));
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletRemove(WalletRemoveBalanceHook hook){
        WalletHandler.getWallet(hook.getRecipient()).removeFromBalance(hook.getToRemove());
        Canary.hooks().callHook(new AccountTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_REMOVE, hook.getToRemove())));
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletSet(WalletSetBalanceHook hook){
        WalletHandler.getWallet(hook.getRecipient()).setBalance(hook.getToSet());
        Canary.hooks().callHook(new AccountTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_SET, hook.getToSet())));
    }

}
