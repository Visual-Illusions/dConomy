package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.AccountBalanceEvent;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public final class WalletBalanceEvent extends AccountBalanceEvent{
    private static final HandlerList handlers = new HandlerList();

    public WalletBalanceEvent(Plugin plugin, Mod_User user){
        super(plugin, user);
    }

    @Override
    public HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}
