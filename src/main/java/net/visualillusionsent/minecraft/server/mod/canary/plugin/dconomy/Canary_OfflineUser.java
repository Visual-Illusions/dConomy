package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.canarymod.api.OfflinePlayer;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;

public final class Canary_OfflineUser implements Mod_User{
    private final OfflinePlayer player;

    public Canary_OfflineUser(OfflinePlayer player){
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
        return ModType.CANARY;
    }

    @Override
    public final boolean hasPermission(String perm){
        return player.hasPermission(perm);
    }

    @Override
    public final OfflinePlayer getPlayer(){
        return player;
    }
}
