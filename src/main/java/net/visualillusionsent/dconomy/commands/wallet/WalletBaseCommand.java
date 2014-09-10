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
            if (!handler.verifyAccount(theUser.getUUID())) {
                dCoBase.translateErrorMessageFor(user, "error.404.account", theUser.getName(), "WALLET");
                return;
            }
            theWallet = handler.getWalletByUUID(theUser.getUUID());
            if (theWallet.isLocked()) {
                dCoBase.translateErrorMessageFor(user, "error.lock.out", theUser.getName(), "WALLET");
            } else {
                dCoBase.translateMessageFor(user, "account.balance.other", theUser.getName(), theWallet.getBalance(), "WALLET");
            }
        } else {
            theWallet = handler.getWalletByUUID(user.getUUID());
            if (theWallet.isLocked()) {
                dCoBase.translateErrorMessageFor(user, "error.lock.out", user.getName(), "WALLET");
            } else {
                dCoBase.translateErrorMessageFor(user, "account.balance", theWallet.getBalance(), "WALLET");
            }
        }
    }
}
