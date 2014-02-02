/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.bukkit;

import net.visualillusionsent.dconomy.bukkit.api.account.wallet.WalletTransactionEvent;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static org.bukkit.event.EventPriority.HIGHEST;

public final class BukkitConomyAPIListener implements Listener {

    BukkitConomyAPIListener(BukkitConomy plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = HIGHEST)
    public final void debugTransaction(WalletTransactionEvent event) {
        dCoBase.debug("WalletTransactionEvent called: " + event.getTransaction());
    }
}
