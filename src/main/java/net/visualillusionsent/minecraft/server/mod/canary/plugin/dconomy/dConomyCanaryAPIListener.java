package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletDebitHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletDepositHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletSetBalanceHook;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletTransactionHook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

public final class dConomyCanaryAPIListener implements PluginListener{

    dConomyCanaryAPIListener(dConomy dCo){
        Canary.hooks().registerListener(this, dCo);
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletDeposit(final WalletDepositHook hook){
        try {
            WalletHandler.getWallet(hook.getRecipient()).deposit(hook.getDeposit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEPOSIT, hook.getDeposit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getSender().getName() + "'. Reason: " + aex.getMessage());
            hook.setResult(aex);
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletDebit(WalletDebitHook hook){
        try {
            WalletHandler.getWallet(hook.getRecipient()).debit(hook.getDebit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEBIT, hook.getDebit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getSender().getName() + "'. Reason: " + aex.getMessage());
            hook.setResult(aex);
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void walletSet(WalletSetBalanceHook hook){
        try {
            WalletHandler.getWallet(hook.getRecipient()).setBalance(hook.getToSet());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(hook.getSender(), hook.getRecipient(), WalletTransaction.ActionType.PLUGIN_SET, hook.getToSet())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + hook.getName() + "' called from Plugin: '" + hook.getSender().getName() + "'. Reason: " + aex.getMessage());
            hook.setResult(aex);
        }
    }

    @HookHandler(priority = Priority.CRITICAL)
    public final void debugTransaction(WalletTransactionHook hook){
        dCoBase.debug("WalletTransactionHook called");
    }
}
