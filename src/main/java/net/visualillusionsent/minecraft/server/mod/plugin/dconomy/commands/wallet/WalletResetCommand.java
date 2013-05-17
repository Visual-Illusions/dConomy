package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountingException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletTransaction;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands.dConomyCommand;

public final class WalletResetCommand extends dConomyCommand{

    public WalletResetCommand(){
        super(1);
    }

    protected final void execute(Mod_User user, String[] args){
        Mod_User theUser = args[0].toUpperCase().equals("SERVER") ? null : dCoBase.getServer().getUser(args[0]);
        if (theUser == null && !args[0].toUpperCase().equals("SERVER")) {
            user.error("error.404.user", args[0]);
            return;
        }
        if (!args[0].toUpperCase().equals("SERVER")) {
            user.error("error.404.wallet", theUser.getName());
            return;
        }
        try {
            double newbal = WalletHandler.getWalletByName(theUser == null ? "SERVER" : theUser.getName()).setBalance(dCoBase.getProperties().getDouble("default.balance"));
            user.error("admin.reset.balance", theUser == null ? "SERVER" : theUser.getName(), newbal);
            dCoBase.getServer().newTransaction(new WalletTransaction(user, theUser == null ? (Mod_User) dCoBase.getServer() : theUser, WalletTransaction.ActionType.ADMIN_SET, dCoBase.getProperties().getDouble("default.balance")));
        }
        catch (AccountingException ae) {
            user.error(ae.getMessage());
        }
    }
}
