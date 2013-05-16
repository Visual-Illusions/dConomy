package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

public final class WalletMySQL_Source extends WalletSQL_Source{

    @Override
    public final boolean load(){
        try {
            testConnection();
            PreparedStatement ps = null;
            dCoBase.debug("Testing Wallet table and creating if needed...");
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `" + wallet_table + "` " + //
                    "(`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL PRIMARY KEY (`owner`))");
            ps.execute();
            ps.close();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while testing Wallets tables...");
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.load();
    }

    @Override
    public final boolean saveAccount(Account wallet){
        try {
            testConnection();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Connection failed while saving Wallet for " + wallet.getOwner());
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.saveAccount(wallet);
    }

    @Override
    public final boolean reloadAccount(Account wallet){
        try {
            testConnection();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Connection failed while reloading Wallet for " + wallet.getOwner());
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.reloadAccount(wallet);
    }

    private final void testConnection() throws SQLException{
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            conn = DriverManager.getConnection("jdbc:mysql://" + dCoBase.getProperties().getString("sql.database.url"), dCoBase.getProperties().getString("sql.user"), dCoBase.getProperties().getString("sql.password"));
        }
    }
}
