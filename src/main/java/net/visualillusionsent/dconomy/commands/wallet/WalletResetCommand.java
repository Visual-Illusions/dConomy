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

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAction;
import net.visualillusionsent.dconomy.api.account.wallet.WalletTransaction;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.dCoBase;

public final class WalletResetCommand extends dConomyCommand {

    public WalletResetCommand() {
        super(1);
    }

    protected final void execute(dConomyUser user, String[] args) {
        dConomyUser theUser = dCoBase.getServer().getUser(args[1]);
        if (theUser == null) {
            dCoBase.translateErrorMessageFor(user, "error.404.user", args[1]);
            return;
        }
        if (!args[0].toUpperCase().equals("SERVER") && !WalletHandler.verifyAccount(theUser.getName())) {
            dCoBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "WALLET");
            return;
        }
        try {
            WalletHandler.getWalletByName(theUser.getName()).setBalance(dCoBase.getProperties().getDouble("default.balance"));
            dCoBase.translateErrorMessageFor(user, "admin.reset.balance", theUser.getName(), "WALLET");
            dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser, WalletAction.ADMIN_RESET, dCoBase.getProperties().getDouble("default.balance")));
        }
        catch (AccountingException ae) {
            user.error(ae.getLocalizedMessage(user.getUserLocale()));
        }
    }
}
