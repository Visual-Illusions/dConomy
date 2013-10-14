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
package net.visualillusionsent.dconomy;

import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.data.DataSourceType;
import net.visualillusionsent.dconomy.data.dCoDataHandler;
import net.visualillusionsent.dconomy.data.dCoProperties;
import net.visualillusionsent.dconomy.data.wallet.WalletSQLiteSource;
import net.visualillusionsent.dconomy.io.logging.dCoLevel;
import net.visualillusionsent.dconomy.modinterface.ModServer;
import net.visualillusionsent.utils.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy Base class.<br>
 * Most things start here.
 *
 * @author Jason (darkdiplomat)
 */
public final class dCoBase {

    private final dCoDataHandler handler;
    private final dCoProperties props;
    private final Logger logger;

    private static dCoBase $;
    private static ModServer server;
    private static float reported_version;

    public dCoBase(dConomy dconomy) {
        if ($ != null) {
            throw new dConomyInitializationError("Already loaded");
        }
        try {
            $ = this;
            this.logger = dconomy.getPluginLogger();
            server = dconomy.getModServer();
            props = new dCoProperties();
            handler = new dCoDataHandler(DataSourceType.valueOf(getProperties().getString("datasource").toUpperCase()));
            testAndMoveLangFiles();

            this.reported_version = dconomy.getReportedVersion();
        } catch (Exception ex) {
            throw new dConomyInitializationError(ex);
        }
    }

    private void testAndMoveLangFiles() {
        boolean mvLangtxt = false, mven_US = false;
        if (!new File("config/dConomy3/lang/").exists()) {
            new File("config/dConomy3/lang/").mkdir();
            mvLangtxt = true;
            mven_US = true;
        } else {
            if (!new File("config/dConomy3/lang/languages.txt").exists()) {
                mvLangtxt = true;
            }
            if (!new File("config/dConomy3/lang/en_US.lang").exists()) {
                mven_US = true;
            }
        }
        if (mvLangtxt) {
            FileUtils.cloneFileFromJar(getJarPath(), "resources/lang/languages.txt", "config/dConomy3/lang/languages.txt");
        }
        if (mven_US) {
            FileUtils.cloneFileFromJar(getJarPath(), "resources/lang/en_US.lang", "config/dConomy3/lang/en_US.lang");
        }
        MessageTranslator.reloadMessages();
    }

    /**
     * Gets the {@link dCoDataHandler} instance
     *
     * @return {@link dCoDataHandler}
     */
    public final static dCoDataHandler getDataHandler() {
        return $.handler;
    }

    /**
     * Gets the {@link dCoProperties} instance
     *
     * @return {@link dCoProperties}
     */
    public final static dCoProperties getProperties() {
        return $.props;
    }

    /**
     * Gets the {@link ModServer} instance
     *
     * @return {@link ModServer}
     */
    public final static ModServer getServer() {
        return server;
    }

    public final static void info(String msg) {
        $.logger.info(msg);
    }

    public final static void info(String msg, Throwable thrown) {
        $.logger.log(Level.INFO, msg, thrown);
    }

    public final static void warning(String msg) {
        $.logger.warning(msg);
    }

    public final static void warning(String msg, Throwable thrown) {
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public final static void severe(String msg) {
        $.logger.severe(msg);
    }

    public final static void severe(String msg, Throwable thrown) {
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public final static void stacktrace(Throwable thrown) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public final static void debug(String msg) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    private final String getJarPath() { // For when the jar isn't dConomy3.jar
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return codeSource.getLocation().toURI().getPath();
        } catch (URISyntaxException ex) {
        }
        return "plugins/dConomy3.jar";
    }

    public final void cleanUp() {
        WalletHandler.cleanUp();
        $.handler.cleanUp();
        if (getDataHandler().getDataSourceType() == DataSourceType.SQLITE) {
            WalletSQLiteSource.cleanUp();
        }
        $ = null;
    }

    public static float getVersion(){
        return reported_version;
    }

    public static final String getServerLocale() {
        return "en_US";
    }
}
