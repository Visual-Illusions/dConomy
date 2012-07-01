package net.visualillusionsent.minecraft.server.mod.canary.plugin.dconomy;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.InformationCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletAddCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletBaseCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletPayCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletRemoveCommand;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet.WalletSetCommand;

public class dConomyCommandListener implements CommandListener{
    private final dConomyCommand infoCmd, walletbase, walletadd, walletremove, walletpay, walletset;

    public dConomyCommandListener(){
        infoCmd = new InformationCommand();
        walletbase = new WalletBaseCommand();
        walletadd = new WalletAddCommand();
        walletpay = new WalletPayCommand();
        walletremove = new WalletRemoveCommand();
        walletset = new WalletSetCommand();
    }

    @Command(aliases = { "dconomy" },
            description = "dConomy Information Command",
            permissions = { "dconomy" },
            toolTip = "/dconomy")
    public final void information(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();
        infoCmd.parseCommand(modcall, args);
    }

    @Command(aliases = { "wallet" },
            description = "Wallet Base",
            permissions = { "dconomy.wallet" },
            toolTip = "/wallet [subcommand] [args]")
    public final void walletBase(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();

        if (!walletbase.parseCommand(modcall, args)) {
            msgrec.notice("/wallet [subcommand] [args]");
        }
    }

    @Command(aliases = { "add" },
            description = "Adds money to user's wallet, use -force to create an account",
            permissions = { "dconomy.admin.wallet.add" },
            toolTip = "/wallet add <amount> <user>",
            parent = "wallet")
    public final void walletAdd(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();

        if (!walletadd.parseCommand(modcall, args)) {
            msgrec.notice("/wallet add <amount> <user> [-force]");
        }
    }

    @Command(aliases = { "pay" },
            description = "Used to pay another user",
            permissions = { "dconomy.wallet.pay" },
            toolTip = "/wallet pay <user> <amount>",
            parent = "wallet")
    public final void walletPay(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();

        if (!walletpay.parseCommand(modcall, args)) {
            msgrec.notice("/wallet pay <amount> <user>");
        }
    }

    @Command(aliases = { "remove" },
            description = "Used to remove money from a user's wallet",
            permissions = { "dconomy.admin.wallet.remove" },
            toolTip = "/wallet remove <user> <amount>",
            parent = "wallet")
    public final void walletRemove(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();

        if (!walletremove.parseCommand(modcall, args)) {
            msgrec.notice("/wallet remove <amount> <user>");
        }
    }

    @Command(aliases = { "set" },
            description = "Used to set the money of a user's wallet, use -force to create an account",
            permissions = { "dconomy.admin.wallet.set" },
            toolTip = "/wallet set <amount> <user> [-force]",
            parent = "wallet")
    public final void walletSet(MessageReceiver msgrec, String[] args){
        Mod_Caller modcall = msgrec instanceof Player ? new Canary_User((Player) msgrec) : new Canary_Console();

        if (!walletset.parseCommand(modcall, args)) {
            msgrec.notice("/wallet set <user> <amount>");
        }
    }
}
