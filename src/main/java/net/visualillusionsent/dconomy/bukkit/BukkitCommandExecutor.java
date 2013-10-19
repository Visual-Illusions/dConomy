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

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.bukkit.api.Bukkit_User;
import net.visualillusionsent.dconomy.commands.dConomyCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletAddCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletBaseCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletLockCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletPayCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletReloadCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletRemoveCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletResetCommand;
import net.visualillusionsent.dconomy.commands.wallet.WalletSetCommand;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.bukkit.VisualIllusionsBukkitPluginInformationCommand;
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

    private final dConomyCommand[] cmds = new dConomyCommand[8];

    BukkitCommandExecutor(BukkitdConomy dCo) {
        super(dCo);
        // Initialize Commands
        cmds[0] = new WalletBaseCommand();
        cmds[1] = new WalletAddCommand();
        cmds[2] = new WalletPayCommand();
        cmds[3] = new WalletRemoveCommand();
        cmds[4] = new WalletSetCommand();
        cmds[5] = new WalletResetCommand();
        cmds[6] = new WalletReloadCommand();
        cmds[7] = new WalletLockCommand();

        // Register commands
        dCo.getCommand("dconomy").setExecutor(this);
        dCo.getCommand("wallet").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        dConomyUser user = sender instanceof Player ? new Bukkit_User((Player) sender) : (dConomyUser) dCoBase.getServer();
        if (label.equals("dconomy")) {
            this.sendInformation(sender);
            return true;
        }
        else if (label.equals("wallet")) {
            if (args.length == 0 && sender.hasPermission("dconomy.wallet.base") && !cmds[0].parseCommand(user, args, false)) {
                sender.sendMessage(ChatColor.RED + "/wallet [subcommand|user] [args]");
            }
            else if (args.length > 0) {
                String sub = args[0].toLowerCase();
                // Check Permissions
                if ((sub.matches("(add|remove|set|reload|reset|lock)") && !sender.hasPermission("dconomy.admin.wallet.".concat(sub)))
                        || (sub.equals("pay") && !sender.hasPermission("dconomy.wallet.pay"))) {
                    sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error."); // Borrow Bukkit's default message
                }
                else if (sub.equals("add") && !cmds[1].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet add <amount> <user> [-force]");
                }
                else if (sub.equals("pay") && !cmds[2].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet pay <amount> <user>");
                }
                else if (sub.equals("remove") && !cmds[3].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet remove <amount> <user>");
                }
                else if (sub.equals("set") && !cmds[4].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet set <amount> <user>");
                }
                else if (sub.equals("reset") && !cmds[5].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet reset <user>");
                }
                else if (sub.equals("reload") && !cmds[6].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet reload <user>");
                }
                else if (sub.equals("lock") && !cmds[7].parseCommand(user, args, true)) {
                    sender.sendMessage(ChatColor.RED + "/wallet lock <yes|no> <user>");
                }
                else if (!cmds[0].parseCommand(user, args, false)) {
                    sender.sendMessage(ChatColor.RED + "/wallet [subcommand|user] [args]");
                }
            }
            return true;
        }
        return false;
    }
}
