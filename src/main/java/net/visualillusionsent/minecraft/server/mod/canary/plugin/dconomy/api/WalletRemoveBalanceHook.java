package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;

public final class WalletRemoveBalanceHook extends AccountRemoveBalanceHook{

    public WalletRemoveBalanceHook(Plugin plugin, Mod_Caller recipient, double toAdd){
        super(plugin, recipient, toAdd);
    }

}
