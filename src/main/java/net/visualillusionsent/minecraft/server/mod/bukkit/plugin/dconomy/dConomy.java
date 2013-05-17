/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletTransactionEvent;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.InformationCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletAddCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletBaseCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletPayCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletRemoveCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletResetCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletSetCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * dConomy main plugin class for Bukkit implementations
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class dConomy extends JavaPlugin{
    private dCoBase base;
    private dConomyCommand infoCmd, walletbase, walletadd, walletremove, walletpay, walletset, walletreset;

    @Override
    public final void onDisable(){
        base.cleanUp(); // Clean Up
    }

    @Override
    public final void onEnable(){
        // Create dCoBase, initializing properties and such
        base = new dCoBase(new Bukkit_Server(getServer(), this), getLogger());
        // Cause Wallets to load
        WalletHandler.initialize();
        // Initialize Listener
        new dConomyBukkitAPIListener(this);
        // Register WalletTransaction
        dCoBase.getServer().registerTransactionHandler(WalletTransactionEvent.class, WalletTransaction.class);

        // Initialize Commands
        infoCmd = new InformationCommand();
        walletbase = new WalletBaseCommand();
        walletadd = new WalletAddCommand();
        walletpay = new WalletPayCommand();
        walletremove = new WalletRemoveCommand();
        walletset = new WalletSetCommand();
        walletreset = new WalletResetCommand();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Mod_User user = sender instanceof Player ? new Bukkit_User((Player) sender) : (Mod_User) dCoBase.getServer();
        if (cmd.getName().equals("dconomy")) {
            infoCmd.parseCommand(user, args, false);
            return true;
        }
        else if (cmd.getName().equals("wallet")) {
            if (args.length == 0) {
                if (!walletbase.parseCommand(user, args, false)) {
                    sender.sendMessage(ChatColor.RED + "/wallet [subcommand|user] [args]");
                }
            }
            else {
                String subcmd = args[0].toLowerCase();
                if (subcmd.equals("add")) {
                    if (!walletadd.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet add <amount> <user> [-force]");
                    }
                }
                else if (subcmd.equals("pay")) {
                    if (!walletpay.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet pay <amount> <user>");
                    }
                }
                else if (subcmd.equals("remove")) {
                    if (!walletremove.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet remove <amount> <user>");
                    }
                }
                else if (subcmd.equals("set")) {
                    if (!walletset.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet set <amount> <user>");
                    }
                }
                else if (subcmd.equals("reset")) {
                    if (!walletreset.parseCommand(user, args, true)) {
                        sender.sendMessage(ChatColor.RED + "/wallet reset <amount> <user>");
                    }
                }
                else {
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
