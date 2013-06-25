/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.canarymod.api.OfflinePlayer;
import net.visualillusionsent.minecraft.server.mod.dconomy.MessageTranslator;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModType;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Bukkit Offline User implementation
 * 
 * @author Jason (darkdiplomat)
 */
public final class Bukkit_User implements IModUser{

    private final Player player;

    /**
     * Constructs a new Bukkit_User
     * 
     * @param player
     *            the {@link OfflinePlayer} to wrap
     */
    public Bukkit_User(Player player){
        this.player = player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName(){
        return player.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean hasPermission(String perm){
        return player.hasPermission(perm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void error(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            player.sendMessage(ChatColor.RED + key);
        }
        else {
            player.sendMessage(ChatColor.RED + MessageTranslator.transFormMessage(key, true, args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void message(String key, Object... args){
        if (args == null || key.trim().isEmpty()) {
            player.sendMessage(key);
        }
        else {
            player.sendMessage(MessageTranslator.transFormMessage(key, true, args));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isConsole(){
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ModType getModType(){
        return ModType.BUKKIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj){
        if (obj instanceof Bukkit_User) {
            return obj == this;
        }
        return obj == player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode(){
        return player.hashCode();
    }

}
