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
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.Colors;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.commands.wallet.*;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.canary.VisualIllusionsCanaryPluginInformationCommand;
import net.visualillusionsent.dconomy.modinterface.ModUser;
import net.visualillusionsent.utils.VersionChecker;

public final class CanarydConomyCommandListener extends VisualIllusionsCanaryPluginInformationCommand {
    private final dConomyCommand walletbase, walletadd, walletremove, walletpay, walletset, walletreset, walletreload, walletlock;

    CanarydConomyCommandListener(CanarydConomy dCo) throws CommandDependencyException {
        super(dCo);
        walletbase = new WalletBaseCommand();
        walletadd = new WalletAddCommand();
        walletpay = new WalletPayCommand();
        walletremove = new WalletRemoveCommand();
        walletset = new WalletSetCommand();
        walletreset = new WalletResetCommand();
        walletreload = new WalletReloadCommand();
        walletlock = new WalletLockCommand();
        Canary.commands().registerCommands(this, dCo, false);
    }

    @Command(aliases = {"dconomy"},
            description = "dConomy Information Command",
            permissions = {""},
            toolTip = "/dconomy")
    public final void information(MessageReceiver msgrec, String[] args) {
        for (String msg : about) {
            if (msg.equals("$VERSION_CHECK$")) {
                VersionChecker vc = plugin.getVersionChecker();
                Boolean isLatest = vc.isLatest();
                if (isLatest == null) {
                    msgrec.message(center(Colors.GRAY + "VersionCheckerError: " + vc.getErrorMessage()));
                } else if (!isLatest) {
                    msgrec.message(center(Colors.GRAY + vc.getUpdateAvailibleMessage()));
                } else {
                    msgrec.message(center(Colors.LIGHT_GREEN + "Latest Version Installed"));
                }
            } else {
                msgrec.message(msg);
            }
        }
    }

    @Command(aliases = {"wallet"},
            description = "Wallet Base",
            permissions = {"dconomy.wallet"},
            toolTip = "/wallet [subcommand] [args]")
    public final void walletBase(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletbase.parseCommand(user, args, true)) {
            msgrec.notice("/wallet [subcommand] [args]");
        }
    }

    @Command(aliases = {"add"},
            description = "Adds money to user's wallet, use -force to create an account",
            permissions = {"dconomy.admin.wallet.add"},
            toolTip = "/wallet add <amount> <user>",
            parent = "wallet")
    public final void walletAdd(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletadd.parseCommand(user, args, true)) {
            msgrec.notice("/wallet add <amount> <user> [-force]");
        }
    }

    @Command(aliases = {"pay"},
            description = "Used to pay another user",
            permissions = {"dconomy.wallet.pay"},
            toolTip = "/wallet pay <amount> <user>",
            parent = "wallet")
    public final void walletPay(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletpay.parseCommand(user, args, true)) {
            msgrec.notice("/wallet pay <amount> <user>");
        }
    }

    @Command(aliases = {"remove"},
            description = "Used to remove money from a user's wallet",
            permissions = {"dconomy.admin.wallet.remove"},
            toolTip = "/wallet remove <user> <amount>",
            parent = "wallet")
    public final void walletRemove(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletremove.parseCommand(user, args, true)) {
            msgrec.notice("/wallet remove <amount> <user>");
        }
    }

    @Command(aliases = {"set"},
            description = "Used to set the money of a user's wallet, use -force to create an account",
            permissions = {"dconomy.admin.wallet.set"},
            toolTip = "/wallet set <amount> <user> [-force]",
            parent = "wallet")
    public final void walletSet(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletset.parseCommand(user, args, true)) {
            msgrec.notice("/wallet set <amount> <user>");
        }
    }

    @Command(aliases = {"reset"},
            description = "Used to reset the money of a user's wallet",
            permissions = {"dconomy.admin.wallet.reset"},
            toolTip = "/wallet reset <user>",
            parent = "wallet")
    public final void walletReset(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletreset.parseCommand(user, args, true)) {
            msgrec.notice("/wallet reset <user>");
        }
    }

    @Command(aliases = {"reload"},
            description = "Used to reload a user's wallet from the datasource",
            permissions = {"dconomy.admin.wallet.reload"},
            toolTip = "/wallet reload <user>",
            parent = "wallet")
    public final void walletReload(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletreload.parseCommand(user, args, true)) {
            msgrec.notice("/wallet reload <user>");
        }
    }

    @Command(aliases = {"lock"},
            description = "Used to lock/unlock a user's wallet from the datasource",
            permissions = {"dconomy.admin.wallet.lock"},
            toolTip = "/wallet lock <yes|no (Or other boolean values)> <user>",
            parent = "wallet")
    public final void walletLock(MessageReceiver msgrec, String[] args) {
        ModUser user = msgrec instanceof Player ? new Canary_User((Player) msgrec) : (ModUser) dCoBase.getServer();

        if (!walletlock.parseCommand(user, args, true)) {
            msgrec.notice("/wallet lock <yes|no> <user>");
        }
    }
}
