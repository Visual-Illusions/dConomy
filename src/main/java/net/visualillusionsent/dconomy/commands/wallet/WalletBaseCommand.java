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

import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.dCoBase;

public final class WalletBaseCommand extends WalletCommand {

    public WalletBaseCommand(WalletHandler handler) {
        super(0, handler);
    }

    protected final void execute(dConomyUser user, String[] args) {
        Wallet theWallet;
        if (args.length == 1 && (user.hasPermission("dconomy.admin.wallet") || !dCoBase.getProperties().getBooleanValue("adminonly.balance.check"))) {
            dConomyUser theUser = dCoBase.getServer().getUser(args[0]);
            if (theUser == null) {
                dCoBase.translateErrorMessageFor(user, "error.404.user", args[1]);
                return;
            }
            if (!args[0].toUpperCase().equals("SERVER") && !handler.verifyAccount(theUser.getName())) {
                dCoBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "WALLET");
                return;
            }
            theWallet = handler.getWalletByName(theUser.getName());
            if (theWallet.isLocked()) {
                dCoBase.translateErrorMessageFor(user, "error.lock.out", theUser.getName(), "WALLET");
            }
            else {
                dCoBase.translateMessageFor(user, "account.balance.other", theUser.getName(), theWallet.getBalance(), "WALLET");
            }
        }
        else {
            theWallet = handler.getWalletByName(user.getName());
            if (theWallet.isLocked()) {
                dCoBase.translateErrorMessageFor(user, "error.lock.out", user.getName(), "WALLET");
            }
            else {
                dCoBase.translateErrorMessageFor(user, "account.balance", theWallet.getBalance(), "WALLET");
            }
        }
    }
}
