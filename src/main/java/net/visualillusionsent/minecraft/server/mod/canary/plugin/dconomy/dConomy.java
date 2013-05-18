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
package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.plugin.Plugin;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy.api.WalletTransactionHook;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.IdConomy;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;

/**
 * dConomy main plugin class for Canary implementations
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class dConomy extends Plugin implements IdConomy{
    private dCoBase base;

    @Override
    public final void disable(){
        base.cleanUp();
    }

    @Override
    public final boolean enable(){
        try {
            base = new dCoBase(this);
            WalletHandler.initialize();
            new dConomyCanaryAPIListener(this);
            new dConomyCanaryCommandListener(this);
            dCoBase.getServer().registerTransactionHandler(WalletTransactionHook.class, WalletTransaction.class);
            return true;
        }
        catch (InitializationError ierr) {
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
    public Logger getPluginLogger(){
        return this.getLogman();
    }

    @Override
    public Mod_Server getModServer(){
        return new Canary_Server(Canary.getServer(), this);
    }
}
