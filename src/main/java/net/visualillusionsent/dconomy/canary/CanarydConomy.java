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
import net.canarymod.commandsys.CommandDependencyException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.dConomyServer;
import net.visualillusionsent.dconomy.api.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.canary.api.Canary_Server;
import net.visualillusionsent.dconomy.canary.api.WalletTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.dConomy;
import net.visualillusionsent.dconomy.dConomyInitializationError;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * dConomy main plugin class for Canary implementations
 *
 * @author Jason (darkdiplomat)
 */
public final class CanarydConomy extends VisualIllusionsCanaryPlugin implements dConomy {

    private static dCoBase base;

    @Override
    public final void disable() {
        base.cleanUp();
    }

    @Override
    public final boolean enable() {
        //super.enable();

        try {
            base = new dCoBase(this);
            WalletHandler.initialize();
            new CanarydConomyAPIListener(this);
            new CanarydConomyCommandListener(this);
            dCoBase.getServer().registerTransactionHandler(WalletTransactionHook.class, WalletTransaction.class);
            return true;
        }
        catch (dConomyInitializationError ierr) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", ierr.getCause());
        }
        catch (CommandDependencyException cdex) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", cdex);
        }
        catch (Exception ex) {
            getLogman().log(Level.SEVERE, "Failed to initialize dConomy", ex);
        }
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
    public float getReportedVersion() {
        return Float.valueOf(getVersion());
    }

    @Override
    public final void check() {
        checkStatus();
        checkVersion();
    }
}
