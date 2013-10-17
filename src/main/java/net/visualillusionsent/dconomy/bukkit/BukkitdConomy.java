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
package net.visualillusionsent.dconomy.bukkit;

import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.bukkit.api.Bukkit_Server;
import net.visualillusionsent.dconomy.bukkit.api.WalletTransactionEvent;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.dConomy;
import net.visualillusionsent.dconomy.modinterface.ModServer;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;

/**
 * dConomy main plugin class for Bukkit implementations
 *
 * @author Jason (darkdiplomat)
 */
public final class BukkitdConomy extends VisualIllusionsBukkitPlugin implements dConomy {
    private dCoBase base;
    private static final String viutils_version;
    private static final String jdom_version;

    static {
        Manifest mf = null;
        try {
            mf = new JarFile(BukkitdConomy.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getManifest();
        }
        catch (IOException ex) {
            // NullPointerException will happen anyways
        }
        viutils_version = mf.getMainAttributes().getValue("VIUtils-Version");
        jdom_version = mf.getMainAttributes().getValue("JDOM2-Version");
    }

    public BukkitdConomy() {
        // Check for VIUtils/jdom2, download as necessary
        File lib = new File("lib/viutils-" + viutils_version + ".jar");
        if (!lib.exists()) {
            try {
                URL website = new URL("http://repo.visualillusionsent.net/net/visualillusionsent/viutils/" + viutils_version + "/viutils-" + viutils_version + ".jar");
                URLConnection conn = website.openConnection();
                ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
                FileOutputStream fos = new FileOutputStream(lib);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            catch (Exception ex) {
                System.out.println("Failed to download VIUtils " + viutils_version);
            }
        }
        lib = new File("lib/jdom2-" + jdom_version + ".jar");
        if (!lib.exists()) {
            try {
                URL website = new URL("http://repo1.maven.org/maven2/org/jdom/jdom2/" + jdom_version + "/jdom2-" + jdom_version + ".jar");
                URLConnection conn = website.openConnection();
                ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
                FileOutputStream fos = new FileOutputStream(lib);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            catch (Exception ex) {
                System.out.println("Failed to download jdom2 " + jdom_version);
            }
        }
        //
    }

    @Override
    public final void onDisable() {
        base.cleanUp(); // Clean Up
    }

    @Override
    public final void onEnable() {
        //super.onEnable(); // Requires VisualIllusionsMinecraftPlugin library 1.0u2
        initialize();
        checkVersion();
        checkStatus();

        // Create dCoBase, initializing properties and such
        base = new dCoBase(this);
        // Cause Wallets to load
        WalletHandler.initialize();
        // Initialize Listener
        new BukkitdConomyAPIListener(this);
        // Initialize Command Executor
        new BukkitCommandExecutor(this);
        // Register WalletTransaction
        dCoBase.getServer().registerTransactionHandler(WalletTransactionEvent.class, WalletTransaction.class);
    }

    @Override
    public Logger getPluginLogger() {
        return this.getLogger();
    }

    @Override
    public ModServer getModServer() {
        return new Bukkit_Server(getServer(), this);
    }

    @Override
    public float getReportedVersion() {
        return Float.valueOf(getDescription().getVersion());
    }

    @Override
    public final void check() {
        checkStatus();
        checkVersion();
    }
}
