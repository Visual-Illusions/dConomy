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
package net.visualillusionsent.dconomy.commands.wallet;

import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.modinterface.ModUser;

public final class WalletResetCommand extends dConomyCommand {

    public WalletResetCommand() {
        super(1);
    }

    protected final void execute(ModUser user, String[] args) {
        ModUser theUser = args[1].toUpperCase().equals("SERVER") ? (ModUser) dCoBase.getServer() : dCoBase.getServer().getUser(args[1]);
        if (theUser == null) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[0].toUpperCase().equals("SERVER") && !WalletHandler.verifyAccount(theUser.getName())) {
            user.error("error.404.account", theUser.getName(), "WALLET");
            return;
        }
        WalletHandler.getWalletByName(theUser.getName()).setBalance(dCoBase.getProperties().getDouble("default.balance"));
        user.error("admin.reset.balance", theUser.getName(), "WALLET");
        dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser, WalletTransaction.ActionType.ADMIN_RESET, dCoBase.getProperties().getDouble("default.balance")));
    }
}
