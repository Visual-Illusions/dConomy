package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class WalletRemoveCommand extends dConomyCommand{

    public WalletRemoveCommand(){
        super(2);
    }

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
        try {
            WalletHandler.getWalletByName(theUser == null ? "SERVER" : theUser.getName()).debit(args[0]);
            user.error("admin.remove.balance", theUser == null ? "SERVER" : theUser.getName(), Double.valueOf(args[0]));
            dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (Mod_User) dCoBase.getServer() : theUser, WalletTransaction.ActionType.ADMIN_REMOVE, Double.parseDouble(args[0])));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
