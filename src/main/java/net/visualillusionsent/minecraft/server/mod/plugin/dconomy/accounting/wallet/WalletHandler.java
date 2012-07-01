package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import java.util.concurrent.ConcurrentHashMap;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public final class WalletHandler{

    private final ConcurrentHashMap<String, Wallet> wallets;
    private static final WalletHandler $;
    private final ServerWallet servwallet;

    static {
        $ = new WalletHandler();
    }

    private WalletHandler(){
        wallets = new ConcurrentHashMap<String, Wallet>();
        servwallet = new ServerWallet(dCoBase.getProperties().getBooleanValue("server.max.always"));
    }

    public static final Wallet getWalletByName(String username){
        if (username.equals("SERVER")) {
            return $.servwallet;
        }
        else if (verifyAccount(username)) {
            return $.wallets.get(username);
        }
        return newWallet(username);
    }

    public static final Wallet getWallet(Mod_Caller caller){
        return getWalletByName(caller.getName());
    }

    public static final void addWallet(Wallet wallet){
        $.wallets.put(wallet.getOwner(), wallet);
    }

    public static final boolean verifyAccount(String username){
        return $.wallets.containsKey(username);
    }

    public static final Wallet newWallet(String username){
        Wallet wallet = new UserWallet(username, dCoBase.getProperties().getDouble("default.balance"));
        addWallet(wallet);
        return wallet;
    }

    public static final void cleanUp(){
        $.wallets.clear();
    }
}
