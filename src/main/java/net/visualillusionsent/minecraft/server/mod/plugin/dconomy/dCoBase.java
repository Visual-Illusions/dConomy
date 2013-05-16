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

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.DataSourceType;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.dCoDataHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.dCoProperties;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletSQLite_Source;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.logging.dCoLevel;
import net.visualillusionsent.utils.ProgramStatus;
import net.visualillusionsent.utils.VersionChecker;

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
    private final String name = "dConomy";
    private final String version_check_URL = "http://visualillusionsent.net/minecraft/plugins/";
    private final VersionChecker vc;
    private ProgramStatus status;
    private String version;
    private String build;

    private static dCoBase $;
    private static Mod_Server server;

    public dCoBase(Mod_Server serv, Logger logger){
        try {
            $ = this;
            this.logger = logger;
            server = serv;
            generateVersion();
            checkStatus();
            vc = new VersionChecker(name, getRawVersion(), build, version_check_URL, status, true);
            checkVersion();
            props = new dCoProperties();
            handler = new dCoDataHandler(DataSourceType.valueOf(getProperties().getString("datasource").toUpperCase()));
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

    public final static String getVersion(){
        if ($.version == null) {
            $.generateVersion();
        }
        return $.version.concat(".").concat($.build);
    }

    public final static String getRawVersion(){
        if ($.version == null) {
            $.generateVersion();
        }
        return $.version;
    }

    public final static ProgramStatus getProgramStatus(){
        return $.status;
    }

    private void generateVersion(){
        try {
            Manifest manifest = getManifest();
            Attributes mainAttribs = manifest.getMainAttributes();
            version = mainAttribs.getValue("Version").replace("-SNAPSHOT", "");
            build = mainAttribs.getValue("Build");
            try {
                status = ProgramStatus.valueOf(mainAttribs.getValue("ProgramStatus"));
            }
            catch (IllegalArgumentException iaex) {
                status = ProgramStatus.UNKNOWN;
            }
        }
        catch (Exception e) {
            warning(e.getMessage());
        }
        if (version == null) {
            version = "UNKNOWN";
        }
        else if (build == null) {
            build = "UNKNOWN";
        }
    }

    private final Manifest getManifest() throws Exception{
        Manifest toRet = null;
        Exception ex = null;
        JarFile jar = null;
        try {
            jar = new JarFile(getJarPath());
            toRet = jar.getManifest();
        }
        catch (Exception e) {
            ex = e;
        }
        finally {
            if (jar != null) {
                try {
                    jar.close();
                }
                catch (IOException e) {}
            }
            if (ex != null) {
                throw ex;
            }
        }
        return toRet;
    }

    private final String getJarPath(){ // For when the jar isn't dConomy3.jar
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return codeSource.getLocation().toURI().getPath();
        }
        catch (URISyntaxException ex) {}
        return "plugins/dConomy3.jar";
    }

    private final void checkStatus(){
        if (status == ProgramStatus.UNKNOWN) {
            severe("dConomy has declared itself as an 'UNKNOWN STATUS' build. Use is not advised and could cause damage to your system!");
        }
        else if (status == ProgramStatus.ALPHA) {
            warning("dConomy has declared itself as a 'ALPHA' build. Production use is not advised!");
        }
        else if (status == ProgramStatus.BETA) {
            warning("dConomy has declared itself as a 'BETA' build. Production use is not advised!");
        }
        else if (status == ProgramStatus.RELEASE_CANDIDATE) {
            info("dConomy has declared itself as a 'Release Candidate' build. Expect some bugs.");
        }
    }

    public final static VersionChecker getVersionChecker(){
        return $.vc;
    }

    private final void checkVersion(){
        Boolean islatest = vc.isLatest();
        if (islatest == null) {
            warning("VersionCheckerError: " + vc.getErrorMessage());
        }
        else if (!vc.isLatest()) {
            warning(vc.getUpdateAvailibleMessage());
            warning("You can view update info @ http://wiki.visualillusionsent.net/dConomy#ChangeLog");
        }
    }

    public final void cleanUp(){
        WalletHandler.cleanUp();
        $.handler.killOutput();
        if (getDataHandler().getDataSourceType() == DataSourceType.SQLITE) {
            WalletSQLite_Source.cleanUp();
        }
    }
}
