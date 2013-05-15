package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;

public final class WalletDepositHook extends AccountDepositHook{

    public WalletDepositHook(Plugin plugin, Mod_User recipient, double toAdd){
        super(plugin, recipient, toAdd);
    }

}
