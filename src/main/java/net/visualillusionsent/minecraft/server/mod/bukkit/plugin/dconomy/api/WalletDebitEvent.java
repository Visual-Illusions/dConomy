package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public final class WalletDebitEvent extends AccountDebitEvent{
    private static final HandlerList handlers = new HandlerList();

    public WalletDebitEvent(Plugin plugin, Mod_User recipient, double toAdd){
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
