package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;

public final class Canary_Plugin implements Mod_Caller{
    private final Plugin plugin;

    public Canary_Plugin(Plugin plugin){
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
        return ModType.CANARY;
    }
}
