package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;

public final class WalletSQLite_Source extends WalletSQL_Source{
    private static WalletSQLite_Source $;
    private final String db_Path = dCoBase.getProperties().getString("sql.database.url");

    public WalletSQLite_Source(){
        if ($ == null) {
            $ = this;
        }
        wallet_table = db_Path + "." + wallet_table;
    }

    @Override
    public final boolean load(){
        Statement st = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:".concat(db_Path));
            st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS `" + wallet_table + "` (`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL, PRIMARY KEY (`owner`))");
            st.close();
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while parsing Wallets table...");
            dCoBase.stacktrace(sqlex);
            return false;
        }
        return super.load();
    }

    public static void cleanUp(){
        try {
            $.conn.close();
        }
        catch (Exception e) {}
        $ = null;
    }

}
