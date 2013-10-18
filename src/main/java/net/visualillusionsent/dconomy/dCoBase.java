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
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.data.DataSourceType;
import net.visualillusionsent.dconomy.data.dCoDataHandler;
import net.visualillusionsent.dconomy.data.dCoProperties;
import net.visualillusionsent.dconomy.data.wallet.WalletSQLiteSource;
import net.visualillusionsent.dconomy.logging.dCoLevel;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy Base class.<br>
 * Most things start here.
 *
 * @author Jason (darkdiplomat)
 */
public final class dCoBase {
    final static String lang_dir = "lang/dConomy3/";

    private final dCoDataHandler handler;
    private final dCoProperties props;
    private final Logger logger;

    private static dCoBase $;
    private static dConomyServer server;
    private static float reported_version;

    public dCoBase(dConomy dconomy) {
        if ($ != null) {
            throw new dConomyInitializationError();
        }
        try {
            $ = this;
            this.logger = dconomy.getPluginLogger();
            server = dconomy.getModServer();
            props = new dCoProperties();
            handler = new dCoDataHandler(DataSourceType.valueOf(getProperties().getString("datasource").toUpperCase()));
            testAndMoveLangFiles();

            reported_version = dconomy.getReportedVersion();
        }
        catch (Exception ex) {
            throw new dConomyInitializationError(ex);
        }
    }

    private void testAndMoveLangFiles() {
        boolean mvLangtxt = false, mven_US = false;
        if (!new File(lang_dir).exists()) {
            new File(lang_dir).mkdirs();
            mvLangtxt = true;
            mven_US = true;
        }
        else {
            if (!new File(lang_dir.concat("languages.txt")).exists()) {
                mvLangtxt = true;
            }
            if (!new File(lang_dir.concat("en_US.lang")).exists()) {
                mven_US = true;
            }
        }
        if (mvLangtxt) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(dCoBase.class), "resources/lang/languages.txt", lang_dir.concat("languages.txt"));
        }
        if (mven_US) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(dCoBase.class), "resources/lang/en_US.lang", lang_dir.concat("en_US.lang"));
        }
        MessageTranslator.reloadMessages();
    }

    /**
     * Gets the {@link dCoDataHandler} instance
     *
     * @return {@link dCoDataHandler}
     */
    public static dCoDataHandler getDataHandler() {
        return $.handler;
    }

    /**
     * Gets the {@link dCoProperties} instance
     *
     * @return {@link dCoProperties}
     */
    public static dCoProperties getProperties() {
        return $.props;
    }

    /**
     * Gets the {@link net.visualillusionsent.dconomy.api.dConomyServer} instance
     *
     * @return {@link net.visualillusionsent.dconomy.api.dConomyServer}
     */
    public static dConomyServer getServer() {
        return server;
    }

    public static String getServerLocale() {
        return $.props.getString("server.locale");
    }

    public static void info(String msg) {
        $.logger.info(msg);
    }

    public static void info(String msg, Throwable thrown) {
        $.logger.log(Level.INFO, msg, thrown);
    }

    public static void warning(String msg) {
        $.logger.warning(msg);
    }

    public static void warning(String msg, Throwable thrown) {
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public static void severe(String msg) {
        $.logger.severe(msg);
    }

    public static void severe(String msg, Throwable thrown) {
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public static void stacktrace(Throwable thrown) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public static void debug(String msg) {
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final void cleanUp() {
        WalletHandler.cleanUp();
        $.handler.cleanUp();
        if (getDataHandler().getDataSourceType() == DataSourceType.SQLITE) {
            WalletSQLiteSource.cleanUp();
        }
        $ = null;
    }

    public static float getVersion() {
        return reported_version;
    }
}
