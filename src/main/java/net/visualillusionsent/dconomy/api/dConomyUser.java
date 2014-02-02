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
     * Sends a error message to the {@code dConomyUser}
     *
     * @param message
     *         the message to send
     */
    void error(String message);

    /**
     * Sends a message to the {@code dConomyUser}
     *
     * @param message
     *         the message to send
     */
    void message(String message);

    /**
     * Checks the {@code dConomyUser} for permissions
     *
     * @param perm
     *         the permission to check for
     *
     * @return {@code true} if has permission; {@code false} otherwise
     */
    boolean hasPermission(String perm);

    /**
     * Gets the User's Locale Code
     *
     * @return the User's Locale Code
     */
    String getUserLocale();
}
