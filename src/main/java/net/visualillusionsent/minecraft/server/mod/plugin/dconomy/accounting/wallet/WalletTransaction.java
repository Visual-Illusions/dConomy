package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

public class WalletTransaction implements AccountTransaction{
    public enum ActionType {
        PAY, //
        ADMIN_ADD, //
        ADMIN_REMOVE, //
        ADMIN_SET, //
        PLUGIN_ADD, //
        PLUGIN_SET, //
        PLUGIN_REMOVE, //
        ;
    }

    private final Mod_Caller sender, recipient;
    private final ActionType type;
    private final double amount;

    public WalletTransaction(Mod_Caller sender, Mod_Caller recipient, ActionType type, double amount){
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
    }

    public final Mod_Caller getSender(){
        return sender;
    }

    public final Mod_Caller getRecipient(){
        return recipient;
    }

    public final ActionType getActionType(){
        return type;
    }

    public final double getAmountChange(){
        return amount;
    }

}
