/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2015 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.canary.api;

import net.canarymod.plugin.Plugin;
import net.visualillusionsent.dconomy.api.dConomyAddOn;
import net.visualillusionsent.dconomy.dCoBase;

import java.util.UUID;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return plugin.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UUID getUUID() {
        return UUID.nameUUIDFromBytes("DCONOMY_ADDON_PLUGIN:".concat(getName()).getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UUID getOfflineUUID() {
        return getUUID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void error(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void message(String message) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale();
    }

    public final boolean isOnline() {
        return false;
    }
}
