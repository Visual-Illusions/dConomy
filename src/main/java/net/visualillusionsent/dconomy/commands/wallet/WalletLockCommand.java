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
package net.visualillusionsent.dconomy.commands.wallet;

import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.utils.BooleanUtils;

public final class WalletLockCommand extends WalletCommand {

    public WalletLockCommand(WalletHandler handler) {
        super(1, handler);
    }

    protected final void execute(dConomyUser user, String[] args) {
        dConomyUser theUser = dCoBase.getServer().getUser(args[1]);
        if (theUser == null) {
            dCoBase.translateErrorMessageFor(user, "error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !handler.verifyAccount(theUser.getName())) {
            dCoBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "WALLET");
            return;
        }
        boolean locked = BooleanUtils.parseBoolean(args[0]);
        handler.getWalletByName(theUser.getName()).setLockOut(locked);
        if (locked) {
            dCoBase.translateMessageFor(user, "admin.account.locked", theUser.getName(), "WALLET");
        }
        else {
            dCoBase.translateMessageFor(user, "admin.account.unlocked", theUser.getName(), "WALLET");
        }
    }
}
