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
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;
import net.visualillusionsent.dconomy.canary.api.account.wallet.WalletTransactionHook;
import net.visualillusionsent.dconomy.dCoBase;

import static net.canarymod.plugin.Priority.CRITICAL;

public final class CanaryConomyAPIListener implements PluginListener {

    CanaryConomyAPIListener(CanaryConomy dCo) {
        Canary.hooks().registerListener(this, dCo);
    }

    @HookHandler(priority = CRITICAL)
    public final void debugTransaction(WalletTransactionHook hook) {
        dCoBase.debug("WalletTransactionHook called");
    }
}
