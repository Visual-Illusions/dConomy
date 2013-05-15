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
package net.visualillusionsent.minecraft.server.mod.interfaces;

/**
 * This file is part of VI-MCMIL.
 * Copyright 2013 Visual Illusions Entertainment.
 * Licensed under the terms of the GNU General Public License Version 3 as published by the Free Software Foundation
 * 
 * @author Jason (darkdiplomat)
 */
public interface Mod_User{

    /**
     * Gets the name of the {@code Mod_User}
     * 
     * @return
     */
    String getName();

    /**
     * Sends a error message to the {@code Mod_User}
     * 
     * @param key
     *            the translation key or plain message to be sent
     * @param args
     *            the arguments for translation
     */
    void error(String key, Object... args);

    /**
     * Sends a message to the {@code Mod_User}
     * 
     * @param key
     *            the translation key or plain message to be sent
     * @param args
     *            the arguments for translation
     */
    void message(String key, Object... args);

    /**
     * Checks the {@code Mod_User} for permissions
     * 
     * @param perm
     *            the permission to check for
     * @return {@code true} if has permission; {@code false} otherwise
     */
    boolean hasPermission(String perm);

    /**
     * Checks if the {@code Mod_User} is the console/server
     * 
     * @return {@code true} if console/server; {@code false} otherwise
     */
    boolean isConsole();

    /**
     * Gets the ModType of the Server
     * 
     * @return the ModType
     * @see ModType
     */
    ModType getModType();
}
