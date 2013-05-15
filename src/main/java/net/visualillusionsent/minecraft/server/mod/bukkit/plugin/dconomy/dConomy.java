package net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.bukkit.plugin.dconomy.api.WalletTransactionEvent;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.InformationCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletAddCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletBaseCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletPayCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletRemoveCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletSetCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class dConomy extends JavaPlugin{
    private dCoBase base;
    private dConomyCommand infoCmd, walletbase, walletadd, walletremove, walletpay, walletset;

    @Override
    public final void onDisable(){
        base.cleanUp();
    }

    @Override
    public final void onEnable(){
        base = new dCoBase(new Bukkit_Server(getServer(), this), getLogger());
        new dConomyBukkitAPIListener(this);
        dCoBase.getServer().registerTransactionHandler(WalletTransactionEvent.class, WalletTransaction.class);
        infoCmd = new InformationCommand();
        walletbase = new WalletBaseCommand();
        walletadd = new WalletAddCommand();
        walletpay = new WalletPayCommand();
        walletremove = new WalletRemoveCommand();
        walletset = new WalletSetCommand();
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
