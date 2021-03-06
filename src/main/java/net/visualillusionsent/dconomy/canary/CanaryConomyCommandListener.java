/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2015 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.TabComplete;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.commands.wallet.*;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPluginInformationCommand;

import java.util.List;

import static net.visualillusionsent.dconomy.commands.wallet.WalletPermissions.*;

public final class CanaryConomyCommandListener extends VisualIllusionsCanaryPluginInformationCommand {
    private final dConomyCommand[] cmds = new dConomyCommand[8];

    CanaryConomyCommandListener(CanaryConomy dCo) throws CommandDependencyException {
        super(dCo);
        cmds[0] = new WalletBaseCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[1] = new WalletAddCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[2] = new WalletPayCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[3] = new WalletRemoveCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[4] = new WalletSetCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[5] = new WalletResetCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[6] = new WalletReloadCommand(dCo.getBaseInstance().getWalletHandler());
        cmds[7] = new WalletLockCommand(dCo.getBaseInstance().getWalletHandler());
        Canary.commands().registerCommands(this, dCo, false);
    }

    @Command(
            aliases = {"dconomy"},
            description = "dConomy Information Command",
            permissions = {""},
            toolTip = "/dconomy"
    )
    public final void information(MessageReceiver msgrec, String[] args) {
        this.sendInformation(msgrec);
    }

    @Command(
            aliases = {"wallet"},
            helpLookup = "wallet",
            description = "Wallet balance view and base command",
            permissions = {WALLET},
            toolTip = "/wallet [subcommand] [args]",
            version = 2
    )
    public final void walletBase(MessageReceiver msgrec, String[] args) {
        if (!cmds[0].parseCommand(getUser(msgrec), args, false)) {
            msgrec.notice("/wallet [subcommand] [args]");
        }
    }

    @Command(
            aliases = {"pay"},
            description = "Used to pay another user",
            permissions = {WALLET$PAY},
            toolTip = "/wallet pay <amount> <user>",
            helpLookup = "wallet pay",
            parent = "wallet"
    )
    public final void walletPay(MessageReceiver msgrec, String[] args) {
        if (!cmds[2].parseCommand(getUser(msgrec), args, false)) {
            msgrec.notice("/wallet pay <amount> <user>");
        }
    }

    @Command(
            aliases = {"add"},
            description = "Adds money to user's wallet, use -force to create an account",
            permissions = {WALLET$ADMIN$ADD},
            toolTip = "/wallet add <amount> <user>",
            helpLookup = "wallet add",
            parent = "wallet"
    )
    public final void walletAdd(MessageReceiver msgrec, String[] args) {
        if (!cmds[1].parseCommand(getUser(msgrec), args, false)) {
            msgrec.notice("/wallet add <amount> <user> [-force]");
        }
    }

    @Command(aliases = {"lock"},
            description = "Used to lock/unlock a user's wallet from the datasource",
            permissions = {WALLET$ADMIN$LOCK},
            toolTip = "/wallet lock <yes|no (Or other boolean values)> <user>",
            helpLookup = "wallet lock",
            parent = "wallet")
    public final void walletLock(MessageReceiver msgrec, String[] args) {
        if (!cmds[7].parseCommand(getUser(msgrec), args, true)) {
            msgrec.notice("/wallet lock <yes|no> <user>");
        }
    }

    @Command(
            aliases = {"reload"},
            description = "Used to reload a user's wallet from the datasource",
            permissions = {WALLET$ADMIN$RELOAD},
            toolTip = "/wallet reload <user>",
            helpLookup = "wallet reload",
            parent = "wallet"
    )
    public final void walletReload(MessageReceiver msgrec, String[] args) {
        if (!cmds[6].parseCommand(getUser(msgrec), args, true)) {
            msgrec.notice("/wallet reload <user>");
        }
    }

    @Command(
            aliases = {"remove"},
            description = "Used to remove money from a user's wallet",
            permissions = {WALLET$ADMIN$REMOVE},
            toolTip = "/wallet remove <user> <amount>",
            helpLookup = "wallet remove",
            parent = "wallet"
    )
    public final void walletRemove(MessageReceiver msgrec, String[] args) {
        if (!cmds[3].parseCommand(getUser(msgrec), args, false)) {
            msgrec.notice("/wallet remove <amount> <user>");
        }
    }

    @Command(
            aliases = {"reset"},
            description = "Used to reset the money of a user's wallet",
            permissions = {WALLET$ADMIN$RESET},
            toolTip = "/wallet reset <user>",
            helpLookup = "wallet reset",
            parent = "wallet"
    )
    public final void walletReset(MessageReceiver msgrec, String[] args) {
        if (!cmds[5].parseCommand(getUser(msgrec), args, true)) {
            msgrec.notice("/wallet reset <user>");
        }
    }

    @Command(
            aliases = {"set"},
            description = "Used to set the money of a user's wallet, use -force to create an account",
            permissions = {WALLET$ADMIN$SET},
            toolTip = "/wallet set <amount> <user> [-force]",
            helpLookup = "wallet set",
            parent = "wallet"
    )
    public final void walletSet(MessageReceiver msgrec, String[] args) {
        if (!cmds[4].parseCommand(getUser(msgrec), args, false)) {
            msgrec.notice("/wallet set <amount> <user>");
        }
    }

    @TabComplete(commands = {"wallet"})
    public final List<String> walletTabComplete(MessageReceiver msgrec, String[] args) {
        return WalletTabComplete.match(getUser(msgrec), args);
    }

    private dConomyUser getUser(MessageReceiver msgrec) {
        return msgrec instanceof Player ? new Canary_User((Player) msgrec) : (dConomyUser) dCoBase.getServer();
    }
}
