package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.event.HandlerList;

public final class WalletDepositEvent extends AccountDepositEvent{
    private static final HandlerList handlers = new HandlerList();

    public WalletDepositEvent(Plugin plugin, Mod_User recipient, double toAdd){
        super(plugin, recipient, toAdd);
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
