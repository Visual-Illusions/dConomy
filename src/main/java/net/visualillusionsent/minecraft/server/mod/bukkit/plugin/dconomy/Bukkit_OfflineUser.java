package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.OfflinePlayer;

/**
 * Bukkit Offline User implementation
 * 
 * @author Jason (darkdiplomat)
 */
public final class Bukkit_OfflineUser implements Mod_User{
    private final OfflinePlayer player;

    public Bukkit_OfflineUser(OfflinePlayer player){
        this.player = player;
    }

    @Override
    public final String getName(){
        return player.getName();
    }

    @Override
    public final void error(String key, Object... args){}

    @Override
    public final void message(String key, Object... args){}

    @Override
    public final boolean isConsole(){
        return false;
    }

    @Override
    public final ModType getModType(){
        return ModType.BUKKIT;
    }

    @Override
    public final boolean hasPermission(String perm){
        return false;
    }
}
