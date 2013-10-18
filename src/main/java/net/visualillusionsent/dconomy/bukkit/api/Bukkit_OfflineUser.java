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

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.OfflinePlayer;

/**
 * Bukkit Offline User implementation
 *
 * @author Jason (darkdiplomat)
 */
public final class Bukkit_OfflineUser implements dConomyUser {

    private final OfflinePlayer player;

    /**
     * Constructs a new Bukkit_OfflineUser
     *
     * @param player
     *         the {@link OfflinePlayer} to wrap
     */
    public Bukkit_OfflineUser(OfflinePlayer player) {
        this.player = player;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return player.getName();
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
    public final boolean hasPermission(String perm) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
