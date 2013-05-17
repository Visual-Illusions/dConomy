/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data.wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.Wallet;

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
                boolean locked = rs.getBoolean("lockedOut");
                new UserWallet(name, balance, locked, this);
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
                    ps = conn.prepareStatement("UPDATE `" + wallet_table + "` SET `balance`=?, `lockedOut`=? WHERE `owner`=?");
                    ps.setDouble(1, wallet.getBalance());
                    ps.setInt(2, ((Wallet) wallet).isLocked() ? 1 : 0);
                    ps.setString(3, wallet.getOwner());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + wallet_table + "` (`owner`,`balance`,`lockedOut`) VALUES(?,?,?)");
                    ps.setString(1, wallet.getOwner());
                    ps.setInt(2, ((Wallet) wallet).isLocked() ? 1 : 0);
                    ps.setDouble(3, wallet.getBalance());
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
                ((Wallet) wallet).setLockOut(rs.getBoolean("lockedOut"));
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
