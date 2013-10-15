/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2013 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.bukkit.api;

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.ModType;
import net.visualillusionsent.dconomy.modinterface.ModUser;
import org.bukkit.plugin.Plugin;

/**
 * Bukkit Plugin wrapper for Mod_User implementation
 *
 * @author darkdiplomat
 */
public final class Bukkit_Plugin implements ModUser {

    private final Plugin plugin;

    /**
     * Constructs a new Bukkit_Plugin wrapper
     *
     * @param plugin
     *         the {@link Plugin} to wrap
     */
    public Bukkit_Plugin(Plugin plugin) {
        this.plugin = plugin;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return plugin.getName();
    }

    /** {@inheritDoc} */
    @Override
    public final void error(String key, Object... args) {
    }

    /** {@inheritDoc} */
    @Override
    public final void message(String key, Object... args) {
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isConsole() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public final ModType getModType() {
        return ModType.BUKKIT;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
