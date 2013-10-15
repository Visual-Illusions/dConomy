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
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.api.OfflinePlayer;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.ModType;
import net.visualillusionsent.dconomy.modinterface.ModUser;

/**
 * Canary Offline User implementation
 *
 * @author Jason (darkdiplomat)
 */
public final class Canary_OfflineUser implements ModUser {

    private final OfflinePlayer player;

    /**
     * Constructs a new Canary_OfflineUser
     *
     * @param player
     *         the {@link OfflinePlayer} to wrap
     */
    public Canary_OfflineUser(OfflinePlayer player) {
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
    public final ModType getModType() {
        return ModType.CANARY;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean hasPermission(String perm) {
        return player.hasPermission(perm);
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }
}
