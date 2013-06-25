/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.dconomy.io.logging;

import java.util.logging.Level;

public final class dCoLevel extends Level{

    private static final long serialVersionUID = 210434042012L;
    private static int baselvl = 15000;
    private static final String RD = "DEBUG-";
    public static final dCoLevel //
            STACKTRACE = new dCoLevel(RD.concat("STACKTRACE"), genLevel()), //
            GENERAL = new dCoLevel(RD.concat("GENERAL"), genLevel());

    protected dCoLevel(String name, int intvalue){
        super(name, intvalue);
    }

    private final static int genLevel(){
        ++baselvl;
        return baselvl;
    }
}
