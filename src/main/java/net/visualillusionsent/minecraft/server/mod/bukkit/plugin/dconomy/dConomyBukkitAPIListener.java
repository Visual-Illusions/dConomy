package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.canarymod.Canary;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletBalanceEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletDebitEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletDepositEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletSetBalanceEvent;
import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletTransactionEvent;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletTransactionHook;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class dConomyBukkitAPIListener implements Listener{

    dConomyBukkitAPIListener(dConomy plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletBalance(WalletBalanceEvent event){
        try {
            event.setBalance(WalletHandler.getWallet(event.getUser()).getBalance());
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getRequester().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletDeposit(WalletDepositEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getDeposit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEPOSIT, event.getDeposit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletDebit(WalletDebitEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getDebit());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_DEBIT, event.getDebit())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void walletSet(WalletSetBalanceEvent event){
        try {
            WalletHandler.getWallet(event.getRecipient()).deposit(event.getToSet());
            Canary.hooks().callHook(new WalletTransactionHook(new WalletTransaction(event.getSender(), event.getRecipient(), WalletTransaction.ActionType.PLUGIN_SET, event.getToSet())));
        }
        catch (AccountingException aex) {
            dCoBase.warning("Failed to handle Hook: '" + event.getEventName() + "' called from Plugin: '" + event.getSender().getName() + "'. Reason: " + aex.getMessage());
            event.setResult(aex);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void debugTransaction(WalletTransactionEvent event){
        dCoBase.debug("WalletTransactionEvent called");
    }
}
