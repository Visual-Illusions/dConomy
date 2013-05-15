package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import org.bukkit.plugin.Plugin;

public final class Bukkit_Plugin implements Mod_User{
    private final Plugin plugin;

    public Bukkit_Plugin(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public final String getName(){
        return plugin.getName();
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
    public boolean hasPermission(String perm){
        return true;
    }
}
