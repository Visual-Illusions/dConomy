/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.api.OfflinePlayer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.visualillusionsent.dconomy.api.dConomyUser;

/**
 * Canary User implementation
 *
 * @author Jason (darkdiplomat)
 */
public final class Canary_User implements dConomyUser {

    private final Player player;

    /**
     * Constructs a new Canary_User
     *
     * @param player
     *         the {@link OfflinePlayer} to wrap
     */
    public Canary_User(Player player) {
        this.player = player;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return player.getName();
    }

    /** {@inheritDoc} */
    @Override
    public final boolean hasPermission(String perm) {
        return player.hasPermission(perm);
    }

    /** {@inheritDoc} */
    @Override
    public final void error(String message) {
        player.notice(message);
    }

    /** {@inheritDoc} */
    @Override
    public final void message(String message) {
        player.message(message);
    }

    @Override
    public String getUserLocale() {
        return player.getLocale();
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Canary_User) {
            return obj == this;
        }
        return obj == player;
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        return player.hashCode();
    }
}
