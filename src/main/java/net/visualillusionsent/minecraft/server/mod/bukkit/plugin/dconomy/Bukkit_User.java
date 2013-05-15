package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.MessageTranslator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Bukkit_User implements Mod_User{

    private final Player player;

    public Bukkit_User(Player player){
        this.player = player;
    }

    @Override
    public final String getName(){
        return player.getName();
    }

    @Override
    public final boolean hasPermission(String perm){
        return player.hasPermission(perm);
    }

    @Override
    public final void error(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            player.sendMessage(ChatColor.RED + key);
        }
        else {
            player.sendMessage(ChatColor.RED + MessageTranslator.transFormMessage(key, true, args));
        }
    }

    @Override
    public final void message(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            player.sendMessage(key);
        }
        else {
            player.sendMessage(MessageTranslator.transFormMessage(key, true, args));
        }
    }

    @Override
    public final boolean isConsole(){
        return false;
    }

    @Override
    public final ModType getModType(){
        return ModType.BUKKIT;
    }

    @Override
    public final boolean equals(Object obj){
        if (obj instanceof Bukkit_User) {
            return obj == this;
        }
        return obj == player;
    }

    @Override
    public final int hashCode(){
        return player.hashCode();
    }

}
