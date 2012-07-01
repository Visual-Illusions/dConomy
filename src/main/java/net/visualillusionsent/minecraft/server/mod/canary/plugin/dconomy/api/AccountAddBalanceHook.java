package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.hook.Hook;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.Canary_Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;

public abstract class AccountAddBalanceHook extends Hook{

    private final Mod_Caller sender;
    private final Mod_Caller recipient;
    private final double toAdd;

    public AccountAddBalanceHook(Plugin plugin, Mod_Caller recipient, double toAdd){
        this.sender = new Canary_Plugin(plugin);
        this.recipient = recipient;
        this.toAdd = toAdd;
    }

    public final Mod_Caller getSender(){
        return sender;
    }

    public final Mod_Caller getRecipient(){
        return recipient;
    }

    public final double getToAdd(){
        return toAdd;
    }
}
