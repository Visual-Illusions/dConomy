package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.UserWallet;

public abstract class WalletSQL_Source implements WalletDataSource{
    protected Connection conn;
    protected String wallet_table = dCoBase.getProperties().getString("sql.wallet.table");

    @Override
    public boolean load(){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int load = 0;
        boolean success = true;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("owner");
                double balance = rs.getDouble("balance");
                new UserWallet(name, balance, this);
                load++;
            }
            dCoBase.info(String.format("Loaded %d Wallets...", load));
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while parsing Wallets file...");
            dCoBase.stacktrace(sqlex);
            success = false;
        }
        finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }
            catch (AbstractMethodError e) {} // SQLite weird stuff
            catch (Exception e) {}
        }
        return success;
    }

    @Override
    public boolean saveAccount(Account wallet){
        boolean success = true;
        synchronized (lock) {
            dCoBase.debug("Saving Wallet for: ".concat(wallet.getOwner()));
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "` WHERE `owner`=?");
                ps.setString(1, wallet.getOwner());
                rs = ps.executeQuery();
                boolean found = rs.next();
                if (found) {
                    ps.close();
                    ps = conn.prepareStatement("UPDATE `" + wallet_table + "` SET `balance`=? WHERE `owner`=?");
                    ps.setDouble(1, wallet.getBalance());
                    ps.setString(2, wallet.getOwner());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + wallet_table + "` (`owner`,`balance`) VALUES(?,?)");
                    ps.setString(1, wallet.getOwner());
                    ps.setDouble(2, wallet.getBalance());
                    ps.execute();
                }
                dCoBase.debug("Wallet saved!");
            }
            catch (SQLException sqlex) {
                dCoBase.severe("Failed to save Wallet for: " + wallet.getOwner());
                dCoBase.stacktrace(sqlex);
            }
            finally {
                try {
                    if (rs != null && !rs.isClosed()) {
                        rs.close();
                    }
                    if (ps != null && !ps.isClosed()) {
                        ps.close();
                    }
                }
                catch (AbstractMethodError e) {} // SQLite weird stuff
                catch (Exception e) {}
            }
        }
        return success;
    }

    @Override
    public boolean reloadAccount(Account wallet){
        boolean success = true;
        dCoBase.debug("Reloading Wallet for: ".concat(wallet.getOwner()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "` WHERE `owner`=?");
            ps.setString(1, wallet.getOwner());
            rs = ps.executeQuery();
            while (rs.next()) {
                wallet.setBalance(rs.getDouble("balance"));
            }
            dCoBase.debug("Reloaded Wallet for: ".concat(wallet.getOwner()));
        }
        catch (SQLException sqlex) {
            dCoBase.severe("SQL Exception while reloading Wallet for: " + wallet.getOwner());
            dCoBase.stacktrace(sqlex);
            success = false;
        }
        finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            }
            catch (AbstractMethodError e) {} // SQLite weird stuff
            catch (Exception e) {}
        }
        return success;
    }
}
