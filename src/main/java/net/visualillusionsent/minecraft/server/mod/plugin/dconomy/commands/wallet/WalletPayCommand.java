package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class WalletPayCommand extends dConomyCommand{

    public WalletPayCommand(){
        super(2);
    }

    @Override
    protected final void execute(Mod_User user, String[] args){
        Mod_User theUser = args[1].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[1]);
        if (theUser == null && !args[1].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[1]);
            return;
        }
        if (!args[1].toUpperCase().equals("SERVER") && !WalletHandler.verifyAccount(theUser.getName())) {
            user.error("error.404.wallet", theUser.getName());
            return;
        }
        Wallet userWallet = WalletHandler.getWalletByName(user.getName());
        Wallet payeeWallet = WalletHandler.getWalletByName(theUser == null ? "SERVER" : theUser.getName());
        try {
            userWallet.testDebit(args[0]);
            payeeWallet.testDeposit(args[0]);
            payeeWallet.deposit(args[0]);
            userWallet.debit(args[0]);
            user.message("paid.user", theUser == null ? "SERVER" : theUser.getName(), Double.parseDouble(args[0]));
            dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (Mod_User) dCoBase.getServer() : theUser, WalletTransaction.ActionType.PAY, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
