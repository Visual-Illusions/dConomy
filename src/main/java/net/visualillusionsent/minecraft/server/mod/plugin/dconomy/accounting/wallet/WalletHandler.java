package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import java.util.concurrent.ConcurrentHashMap;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.DataSourceType;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletDataSource;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletMySQL_Source;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletSQLite_Source;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet.WalletXMLSource;

public final class WalletHandler{

    private final ConcurrentHashMap<String, Wallet> wallets;
    private static final WalletHandler $;
    private final ServerWallet servwallet;
    private final WalletDataSource source;
    private static boolean init;

    static {
        $ = new WalletHandler(dCoBase.getDataHandler().getDataSourceType());
    }

    private WalletHandler(DataSourceType type){
        wallets = new ConcurrentHashMap<String, Wallet>();
        servwallet = new ServerWallet(dCoBase.getProperties().getBooleanValue("server.max.always"));
        if (type == DataSourceType.MYSQL) {
            source = new WalletMySQL_Source();
        }
        else if (type == DataSourceType.SQLITE) {
            source = new WalletSQLite_Source();
        }
        else {
            source = new WalletXMLSource();
        }
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

    public static final Wallet getWallet(Mod_User caller){
        return getWalletByName(caller.getName());
    }

    public static final void addWallet(Wallet wallet){
        $.wallets.put(wallet.getOwner(), wallet);
    }

    public static final boolean verifyAccount(String username){
        return $.wallets.containsKey(username);
    }

    public static final Wallet newWallet(String username){
        Wallet wallet = new UserWallet(username, dCoBase.getProperties().getDouble("default.balance"), $.source);
        addWallet(wallet);
        return wallet;
    }

    public static final void initialize(){
        if (!init) {
            $.source.load();
            init = true;
        }
    }

    public static final void cleanUp(){
        $.wallets.clear();
    }
}
