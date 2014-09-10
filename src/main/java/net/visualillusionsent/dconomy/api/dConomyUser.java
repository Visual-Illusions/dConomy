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
/* Copyright 2013 Visual Illusions Entertainment.
 * This file is part of Visual Illusions Minecraft Mod Interface Library (VI-MCMIL).
 * VI-MCMIL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * VI-MCMIL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with VI-MCMIL.
 * If not, see http://www.gnu.org/licenses/gpl.html */
package net.visualillusionsent.dconomy.api;

import java.util.UUID;

/**
 * dConomy User Interface
 *
 * @author Jason (darkdiplomat)
 */
public interface dConomyUser {

    /**
     * Gets the name of the {@code dConomyUser}
     *
     * @return the {@code dConomyUser} name
     */
    String getName();

    /**
     * Gets the {@link UUID} of the {@code dConomyUser}
     *
     * @return the {@code dConomyUser} {@link UUID}
     */
    UUID getUUID();

    /**
     * Sends a error message to the {@code dConomyUser}
     *
     * @param message the message to send
     */
    void error(String message);

    /**
     * Sends a message to the {@code dConomyUser}
     *
     * @param message the message to send
     */
    void message(String message);

    /**
     * Checks the {@code dConomyUser} for permissions
     *
     * @param perm the permission to check for
     * @return {@code true} if has permission; {@code false} otherwise
     */
    boolean hasPermission(String perm);

    /**
     * Gets the User's Locale Code
     *
     * @return the User's Locale Code
     */
    String getUserLocale();

    boolean isOnline();
}
