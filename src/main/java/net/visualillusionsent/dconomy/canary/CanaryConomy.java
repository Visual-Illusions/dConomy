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
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.canary.api.Canary_Server;
import net.visualillusionsent.dconomy.canary.api.account.wallet.WalletTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.dConomy;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

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
        super.disable();
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
                getLogman().error("dConomy failed to start. Reason: ".concat(reason), ex);
            }
            else {
                getLogman().error("dConomy failed to start. Reason: ".concat(reason));
            }
        }
        // And its a failure!
        return false;
    }

    @Override
    public Logger getPluginLogger() {
        return logger;
    }

    @Override
    public dConomyServer getModServer() {
        return new Canary_Server(Canary.getServer(), this);
    }

    @Override
    public float getReportedVersion() {
        return Float.valueOf(getVersion().substring(0, getVersion().lastIndexOf('.')));
    }

    @Override
    public long getReportedRevision() {
        String temp = getVersion().replace("-SNAPSHOT", "");
        return Long.valueOf(temp.substring(temp.lastIndexOf('.') + 1, temp.length()));
    }

    public static dCoBase getdCoBase() {
        return base;
    }

    @Override
    public final boolean debugEnabled() {
        return debug;
    }

    final dCoBase getBaseInstance() {
        return base;
    }
}
