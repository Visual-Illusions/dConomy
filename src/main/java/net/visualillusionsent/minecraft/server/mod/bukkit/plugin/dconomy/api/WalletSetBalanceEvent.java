package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public final class WalletSetBalanceEvent extends AccountSetBalanceEvent{
    private static final HandlerList handlers = new HandlerList();

    public WalletSetBalanceEvent(Plugin plugin, Mod_User recipient, double toRemove){
        super(plugin, recipient, toRemove);
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
