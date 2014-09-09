/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.bukkit;

import net.milkbowl.vault.economy.Economy;
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
        String vi_url = MessageFormat.format("http://repo2.visualillusionsent.net/repository/public/net/visualillusionsent/viutils/{0}/viutils-{0}.jar", viutils_version);
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
        return Float.valueOf(getPluginVersion().substring(0, getPluginVersion().lastIndexOf('.')));
    }

    @Override
    public long getReportedRevision() {
        String temp = getPluginVersion().replace("-SNAPSHOT", "");
        return Long.valueOf(temp.substring(temp.lastIndexOf('.') + 1, temp.length()));
    }

    @Override
    public boolean debugEnabled() {
        return debug;
    }

    final dCoBase getBaseInstance() {
        return base;
    }
}
