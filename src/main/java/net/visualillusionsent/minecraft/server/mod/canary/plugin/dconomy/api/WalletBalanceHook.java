package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;

public final class WalletBalanceHook extends AccountBalanceHook{

    public WalletBalanceHook(Plugin plugin, Mod_User user){
        super(plugin, user);
    }

}
