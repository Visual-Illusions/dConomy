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
package net.visualillusionsent.dconomy.commands.wallet;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAction;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

public final class WalletRemoveCommand extends WalletCommand {

    public WalletRemoveCommand(WalletHandler handler) {
        super(2, handler);
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
        try {
            handler.getWalletByName(theUser.getName()).debit(args[0]);
            dCoBase.translateErrorMessageFor(user, "admin.remove.balance", theUser.getName(), Double.valueOf(args[0]), "WALLET");
            dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser, WalletAction.ADMIN_REMOVE, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getLocalizedMessage(user.getUserLocale()));
        }
    }
}
