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
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.dCoBase;

/**
 * Canary Plugin wrapper for {@link dConomyAddOn} implementation
 *
 * @author darkdiplomat
 */
public final class Canary_Plugin implements dConomyAddOn {

    private final Plugin plugin;

    /**
     * Constructs a new Canary_Plugin wrapper
     *
     * @param plugin
     *         the {@link Plugin} to wrap
     */
    public Canary_Plugin(Plugin plugin) {
        this.plugin = plugin;
    }

    /** {@inheritDoc} */
    @Override
    public final String getName() {
        return plugin.getName();
    }

    /** {@inheritDoc} */
    @Override
    public final void error(String message) {
    }

    /** {@inheritDoc} */
    @Override
    public final void message(String message) {
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
