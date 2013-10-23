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
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.canary.api.Canary_Server;
import net.visualillusionsent.dconomy.canary.api.account.wallet.WalletTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.dConomy;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy main plugin class for Canary implementations
 *
 * @author Jason (darkdiplomat)
 */
public final class CanaryConomy extends VisualIllusionsCanaryPlugin implements dConomy {

    private static dCoBase base;

    @Override
    public final void disable() {
        if (base != null) {
            base.cleanUp();
        }
    }

    @Override
    public final boolean enable() {
        super.enable();

        try {
            // Create dCoBase, initializing properties and such
            base = new dCoBase(this);
            // Initialize Listener
            new CanaryConomyAPIListener(this);
            // Initialize Command Listener
            new CanaryConomyCommandListener(this);
            // Initialize MOTD Listener
            new CanaryConomyMOTDListener(this);
            // Register WalletTransaction
            dCoBase.getServer().registerTransactionHandler(WalletTransactionHook.class, WalletTransaction.class);
            // Good, return true
            return true;
        }
        catch (Exception ex) {
            String reason = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
            if (debug) { // Only stack trace if debugging
                getLogman().log(Level.SEVERE, "dConomy failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogman().severe("dConomy failed to start. Reason: ".concat(reason));
            }
        }
        // And its a failure!
        return false;
    }

    @Override
    public Logger getPluginLogger() {
        return this.getLogman();
    }

    @Override
    public dConomyServer getModServer() {
        return new Canary_Server(Canary.getServer(), this);
    }

    public static dCoBase getdCoBase() {
        return base;
    }

    @Override
    public final float getReportedVersion() {
        return Float.valueOf(getMajorMinor());
    }

    @Override
    public final long getReportedRevision() {
        return Long.valueOf(getRevision());
    }

    @Override
    public final boolean debugEnabled() {
        return debug;
    }

    final dCoBase getBaseInstance() {
        return base;
    }
}
