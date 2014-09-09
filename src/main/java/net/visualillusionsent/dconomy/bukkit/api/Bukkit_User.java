/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.bukkit.api;

import net.canarymod.api.OfflinePlayer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.ChatFormat;
import org.bukkit.entity.Player;

/**
 * Bukkit User implementation
 *
 * @author Jason (darkdiplomat)
 */
public final class Bukkit_User implements dConomyUser {

    private final Player player;

    /**
     * Constructs a new Bukkit_User
     *
     * @param player
     *         the {@link OfflinePlayer} to wrap
     */
    public Bukkit_User(Player player) {
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
        player.sendMessage(ChatFormat.LIGHT_RED.concat(message));
    }

    /** {@inheritDoc} */
    @Override
    public final void message(String message) {
        player.sendMessage(message);
    }

    /** {@inheritDoc} */
    @Override
    public String getUserLocale() {
        return dCoBase.getServerLocale(); // Bukkit doesn't have access to this field...
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof Bukkit_User) {
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
