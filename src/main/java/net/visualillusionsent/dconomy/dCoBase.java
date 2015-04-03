/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2015 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy;

import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAPIListener;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.data.DataSourceException;
import net.visualillusionsent.dconomy.data.DataSourceType;
import net.visualillusionsent.dconomy.data.dCoDataHandler;
import net.visualillusionsent.dconomy.data.dCoProperties;
import net.visualillusionsent.dconomy.data.wallet.WalletSQLiteSource;
import net.visualillusionsent.dconomy.logging.dCoLevel;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;

import java.util.UUID;
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
    private final dCoDataHandler dat;
    private final dCoProperties props;
    private final Logger logger;
    private final dConomyTranslator translator;
    private final WalletHandler wh;

    private static dCoBase $;
    private static dConomyServer server;

    public dCoBase(dConomy dconomy) throws DataSourceException {
        if ($ != null) {
            throw new PluginInitializationException("dConomy's dCoBase is already initialized.");
        }
        $ = this;
        this.plugin = dconomy;
        this.logger = dconomy.getPluginLogger();
        server = dconomy.getModServer();
        props = new dCoProperties();
        dat = new dCoDataHandler(DataSourceType.valueOf(getProperties().getString("datasource").toUpperCase()));
        wh = new WalletHandler(dat.getDataSourceType());
        WalletAPIListener.setHandler(wh);
        translator = new dConomyTranslator(dconomy);
    }

    /**
     * Gets the {@link dCoDataHandler} instance
     *
     * @return {@link dCoDataHandler}
     */
    public static dCoDataHandler getDataHandler() {
        return $.dat;
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

    static boolean updateLang() {
        return $.props.getBooleanValue("update.lang");
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
        $.wh.cleanUp();
        $.dat.cleanUp();
        if (getDataHandler().getDataSourceType() == DataSourceType.SQLITE) {
            WalletSQLiteSource.cleanUp();
        }
        $ = null;
    }

    public static boolean isNewerThan(float version, long revision) {
        return isNewerThan(version, revision, true);
    }

    public static boolean isNewerThan(float version, long revision, boolean checkRevision) {
        if ($.plugin.getReportedVersion() > version) {
            return true;
        }
        else if (checkRevision && $.plugin.getReportedVersion() == version && $.plugin.getReportedRevision() > revision) {
            return true;
        }
        return false;
    }

    public static float getVersion() {
        return $.plugin.getReportedVersion();
    }

    public static long getRevision() {
        return $.plugin.getReportedRevision();
    }

    public static String getMoneyName() {
        return $.props.getString("money.name");
    }

    public final WalletHandler getWalletHandler() {
        return wh;
    }

    public static String translateUUIDToName(UUID uuid) {
        dConomyUser dcouser = getServer().getUserFromUUID(uuid);
        if (dcouser != null) {
            return dcouser.getName();
        }
        return uuid.toString();
    }

}
