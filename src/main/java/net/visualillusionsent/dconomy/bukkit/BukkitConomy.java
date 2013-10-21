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

import net.milkbowl.vault.economy.Economy;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.bukkit.api.Bukkit_Server;
import net.visualillusionsent.dconomy.bukkit.api.Economy_dConomy;
import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletTransactionEvent;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.dConomy;
import net.visualillusionsent.minecraft.plugin.VisualIllusionsMinecraftPlugin;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy main plugin class for Bukkit implementations
 *
 * @author Jason (darkdiplomat)
 */
public final class BukkitConomy extends VisualIllusionsBukkitPlugin implements dConomy {
    private dCoBase base;
    private boolean vault_linked = false;
    private Economy_dConomy eco_dco;

    static {
        // Check for VIUtils/JDOM2, download as necessary
        Manifest mf = null;
        try {
            mf = new JarFile(BukkitConomy.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getManifest();
        }
        catch (IOException ex) {
            // NullPointerException will happen anyways
        }
        String viutils_version = mf.getMainAttributes().getValue("VIUtils-Version");
        String vi_url = MessageFormat.format("http://repo.visualillusionsent.net/net/visualillusionsent/viutils/{0}/viutils-{0}.jar", viutils_version);
        String jdom_version = mf.getMainAttributes().getValue("JDOM2-Version");
        String jdom_url = MessageFormat.format("http://repo1.maven.org/maven2/org/jdom/jdom2/{0}/jdom2-{0}.jar", jdom_version);
        try {
            VisualIllusionsMinecraftPlugin.getLibrary("dConomy", "viutils", viutils_version, new URL(vi_url), Bukkit.getLogger());
            VisualIllusionsMinecraftPlugin.getLibrary("dConomy", "jdom2", jdom_version, new URL(jdom_url), Bukkit.getLogger());
        }
        catch (MalformedURLException e) {
            // the URLs are correct
        }
        //
    }

    @Override
    public final void onDisable() {
        if (base != null) {
            base.cleanUp(); // Clean Up
        }
        if (vault_linked) {
            Bukkit.getServicesManager().unregister(Economy.class, eco_dco);
        }
    }

    @Override
    public final void onEnable() {
        super.onEnable();

        try {
            // Create dCoBase, initializing properties and such
            this.base = new dCoBase(this);
            // Cause Wallets to load
            WalletHandler.initialize();
            // Initialize Listener
            new BukkitConomyAPIListener(this);
            // Initialize Command Executor
            new BukkitConomyCommandExecutor(this);
            // Register WalletTransaction
            dCoBase.getServer().registerTransactionHandler(WalletTransactionEvent.class, WalletTransaction.class);

            // And try to link Vault
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
                eco_dco = new Economy_dConomy(vault);
                Bukkit.getServicesManager().register(Economy.class, eco_dco, vault, ServicePriority.Normal);
                vault_linked = true;
            }
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogger().log(Level.SEVERE, "dConomy failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogger().severe("dConomy failed to start. Reason: ".concat(reason));
            }
            // And its a failure!
            die();
        }
    }

    @Override
    public Logger getPluginLogger() {
        return this.getLogger();
    }

    @Override
    public dConomyServer getModServer() {
        return new Bukkit_Server(getServer(), this);
    }

    @Override
    public float getReportedVersion() {
        return Float.valueOf(getMajorMinor());
    }

    @Override
    public final long getReportedRevision() {
        return Long.valueOf(getRevision());
    }

    @Override
    public boolean debugEnabled() {
        return debug;
    }
}
