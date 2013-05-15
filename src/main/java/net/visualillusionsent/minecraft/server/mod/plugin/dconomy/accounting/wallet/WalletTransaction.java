package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

public final class WalletTransaction extends AccountTransaction{
    public enum ActionType {
        PAY, //
        ADMIN_ADD, //
        ADMIN_REMOVE, //
        ADMIN_SET, //
        PLUGIN_DEBIT, //
        PLUGIN_DEPOSIT, //
        PLUGIN_SET, //
        ;
    }

    private final Mod_User sender, recipient;
    private final ActionType type;
    private final double amount;

    public WalletTransaction(Mod_User sender, Mod_User recipient, ActionType type, double amount){
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
    }

    public final Mod_User getSender(){
        return sender;
    }

    public final Mod_User getRecipient(){
        return recipient;
    }

    public final ActionType getActionType(){
        return type;
    }

    public final double getAmountChange(){
        return amount;
    }

}
