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
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.data.DataSourceException;
import net.visualillusionsent.dconomy.data.DataSourceType;
import net.visualillusionsent.dconomy.data.dCoDataHandler;
import net.visualillusionsent.dconomy.data.dCoProperties;
import net.visualillusionsent.dconomy.data.wallet.WalletSQLiteSource;
import net.visualillusionsent.dconomy.logging.dCoLevel;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy Base class.<br>
 * Most things start here.
 *
 * @author Jason (darkdiplomat)
 */
public final class dCoBase {
    private final dConomy plugin;
    private final dCoDataHandler handler;
    private final dCoProperties props;
    private final Logger logger;
    private final MessageTranslator translator;

    private static dCoBase $;
    private static dConomyServer server;
    private static float reported_version;
    private static long reported_revision;

    public dCoBase(dConomy dconomy) throws DataSourceException {
        if ($ != null) {
            throw new PluginInitializationException("dConomy's dCoBase is already initialized.");
        }
        $ = this;
        this.plugin = dconomy;
        this.logger = dconomy.getPluginLogger();
        server = dconomy.getModServer();
        props = new dCoProperties();
        handler = new dCoDataHandler(DataSourceType.valueOf(getProperties().getString("datasource").toUpperCase()));
        translator = new MessageTranslator();

        reported_version = dconomy.getReportedVersion();
        reported_revision = dconomy.getReportedRevision();
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

    /**
     * Sends a dConomy Translated message to a user
     *
     * @param user
     *         the {@link dConomyUser} to send the message to
     * @param key
     *         the key to look up
     * @param args
     *         the arguments to format the message with
     */
    public static void translateMessageFor(dConomyUser user, String key, Object... args) {
        user.message($.translator.translate(key, user.getUserLocale(), args));
    }

    /**
     * Sends a dConomy Translated error message to a user
     *
     * @param user
     *         the {@link dConomyUser} to send the message to
     * @param key
     *         the key to look up
     * @param args
     *         the arguments to format the message with
     */
    public static void translateErrorMessageFor(dConomyUser user, String key, Object... args) {
        user.error($.translator.translate(key, user.getUserLocale(), args));
    }

    public static String translateMessage(String key, String locale, Object... args) {
        return $.translator.translate(key, locale, args);
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
        if ($.plugin.debugEnabled()) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public static void debug(String msg) {
        if ($.plugin.debugEnabled()) {
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

    public static boolean isNewerThan(float version, long revision) {
        if (reported_version > version) {
            return true;
        }
        else if (reported_revision > revision) {
            return true;
        }
        return false;
    }

    public static float getVersion() {
        return reported_version;
    }

    public static long getRevision() {
        return reported_revision;
    }

    public static String getMoneyName() {
        return $.props.getString("money.name");
    }
}
