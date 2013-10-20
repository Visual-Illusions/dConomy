package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.motd.MOTDKey;
import net.canarymod.motd.MessageOfTheDayListener;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.dconomy.canary.api.Canary_User;
import net.visualillusionsent.dconomy.dCoBase;

/**
 * Canary dConomy Message Of The Day Listener
 *
 * @author Jason (darkdiplomat)
 */
public final class CanaryConomyMOTDListener implements MessageOfTheDayListener {

    public CanaryConomyMOTDListener(CanaryConomy cdConomy) {
        Canary.motd().registerMOTDListener(this, cdConomy, false);
    }

    @MOTDKey(key = "{wallet.balance}")
    public String wallet_balance(MessageReceiver msgrec) {
        return String.valueOf(WalletHandler.getWallet(asUser(msgrec)).getBalance());
    }

    private dConomyUser asUser(MessageReceiver msgrec) {
        return msgrec instanceof Player ? new Canary_User((Player) msgrec) : dCoBase.getServer();
    }
}
