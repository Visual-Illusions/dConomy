package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;

public final class WalletSetBalanceHook extends AccountSetBalanceHook{

    public WalletSetBalanceHook(Plugin plugin, Mod_User recipient, double toRemove){
        super(plugin, recipient, toRemove);
    }

}
