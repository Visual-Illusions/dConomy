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
/*
 * This file is part of SearchIds.
 *
 * Copyright © 2012-2013 Visual Illusions Entertainment
 *
 * SearchIds is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * SearchIds is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with SearchIds.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.bukkit;

import net.visualillusionsent.dconomy.bukkit.api.Bukkit_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.commands.wallet.*;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPluginInformationCommand;
import net.visualillusionsent.dconomy.modinterface.ModUser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command Executor for Bukkit
 *
 * @author Jason (darkdiplomat)
 */
public class BukkitCommandExecutor extends VisualIllusionsBukkitPluginInformationCommand {

    private dConomyCommand walletbase, walletadd, walletremove, walletpay, walletset, walletreset, walletreload, walletlock;

    BukkitCommandExecutor(BukkitdConomy dCo) {
        super(dCo);
        // Initialize Commands
        walletbase = new WalletBaseCommand();
        walletadd = new WalletAddCommand();
        walletpay = new WalletPayCommand();
        walletremove = new WalletRemoveCommand();
        walletset = new WalletSetCommand();
        walletreset = new WalletResetCommand();
        walletreload = new WalletReloadCommand();
        walletlock = new WalletLockCommand();

        // Register commands
        dCo.getCommand("dconomy").setExecutor(this);
        dCo.getCommand("wallet").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ModUser user = sender instanceof Player ? new Bukkit_User((Player) sender) : (ModUser) dCoBase.getServer();
        if (label.equals("dconomy")) {

            return true;
        } else if (label.equals("wallet")) {
            if (args.length == 0) {
                if (!walletbase.parseCommand(user, args, false)) {
                    sender.sendMessage(ChatColor.RED + "/wallet [subcommand|user] [args]");
                }
            } else {
                String subcmd = args[0].toLowerCase();
                if (subcmd.equals("add")) {
                    if (!walletadd.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet add <amount> <user> [-force]");
                    }
                } else if (subcmd.equals("pay")) {
                    if (!walletpay.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet pay <amount> <user>");
                    }
                } else if (subcmd.equals("remove")) {
                    if (!walletremove.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet remove <amount> <user>");
                    }
                } else if (subcmd.equals("set")) {
                    if (!walletset.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet set <amount> <user>");
                    }
                } else if (subcmd.equals("reset")) {
                    if (!walletreset.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet reset <user>");
                    }
                } else if (subcmd.equals("reload")) {
                    if (!walletreload.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet reload <user>");
                    }
                } else if (subcmd.equals("lock")) {
                    if (!walletlock.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet lock <yes|no> <user>");
                    }
                } else {
                    if (!walletbase.parseCommand(user, args, false)) {
                        sender.sendMessage(ChatColor.RED + "/wallet [subcommand|user] [args]");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
