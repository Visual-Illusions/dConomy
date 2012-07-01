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

import java.util.logging.Logger;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

/**
 * This file is part of VI-MCMIL.
 * Copyright 2013 Visual Illusions Entertainment.
 * Licensed under the terms of the GNU General Public License Version 3 as published by the Free Software Foundation
 * 
 * @author Jason (darkdiplomat)
 */
public interface Mod_Server{

    Mod_User getUser(String name);

    Logger getServerLogger();

    ModType getModType();

    void newTransaction(AccountTransaction transaction);

    Mod_Caller getServerUser();
}
