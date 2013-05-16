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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.DataSourceType;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.dCoDataHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.dCoProperties;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.logging.dCoLevel;

/**
 * dConomy Base class.<br>
 * Most things start here.
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class dCoBase{

    private final dCoDataHandler handler;
    private final dCoProperties props;
    private final Logger logger;
    private static dCoBase $;
    private static Mod_Server server;

    public dCoBase(Mod_Server serv, Logger logger){
        try {
            $ = this;
            this.logger = logger;
            server = serv;
            props = new dCoProperties();
            handler = new dCoDataHandler(DataSourceType.XML);
        }
        catch (Exception ex) {
            throw new InitializationError(ex);
        }
    }

    /**
     * Gets the {@link dCoDataHandler} instance
     * 
     * @return {@link dCoDataHandler}
     */
    public final static dCoDataHandler getDataHandler(){
        return $.handler;
    }

    /**
     * Gets the {@link dCoProperties} instance
     * 
     * @return {@link dCoProperties}
     */
    public final static dCoProperties getProperties(){
        return $.props;
    }

    /**
     * Gets the {@link Mod_Server} instance
     * 
     * @return {@link Mod_Server}
     */
    public final static Mod_Server getServer(){
        return server;
    }

    public final static void info(String msg){
        $.logger.info(msg);
    }

    public final static void info(String msg, Throwable thrown){
        $.logger.log(Level.INFO, msg, thrown);
    }

    public final static void warning(String msg){
        $.logger.warning(msg);
    }

    public final static void warning(String msg, Throwable thrown){
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public final static void severe(String msg){
        $.logger.severe(msg);
    }

    public final static void severe(String msg, Throwable thrown){
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public final static void stacktrace(Throwable thrown){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public final static void debug(String msg){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final void cleanUp(){
        WalletHandler.cleanUp();
    }
}
