/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2013 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.data.wallet;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.dCoBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class WalletSQLSource extends WalletDataSource {
    protected Connection conn;
    protected String wallet_table = dCoBase.getProperties().getString("sql.wallet.table");

    public WalletSQLSource(WalletHandler wallet_handler) {
        super(wallet_handler);
    }

    @Override
    public boolean load() {
        boolean success = true;
        synchronized (lock) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            int load = 0;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "`");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("owner");
                    double balance = rs.getDouble("balance");
                    boolean locked = rs.getBoolean("lockedOut");
                    wallet_handler.addWallet(new UserWallet(name, balance, locked, this));
                    load++;
                }
                dCoBase.info(String.format("Loaded %d Wallets...", load));
            }
            catch (SQLException sqlex) {
                dCoBase.severe("SQL Exception while parsing Wallets table...");
                dCoBase.stacktrace(sqlex);
                success = false;
            }
            finally {
                try {
                    if (!WalletSQLiteSource.class.isInstance(this)) {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                        if (ps != null && !ps.isClosed()) {
                            ps.close();
                        }
                    }
                }
                catch (Exception e) {
                }
            }
        }
        return success;
    }

    @Override
    public boolean saveAccount(Wallet wallet) {
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
                    ps.setInt(2, wallet.isLocked() ? 1 : 0);
                    ps.setString(3, wallet.getOwner());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + wallet_table + "` (`owner`,`balance`,`lockedOut`) VALUES(?,?,?)");
                    ps.setString(1, wallet.getOwner());
                    ps.setInt(2, wallet.isLocked() ? 1 : 0);
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
                    if (!WalletSQLiteSource.class.isInstance(this)) {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                        if (ps != null && !ps.isClosed()) {
                            ps.close();
                        }
                    }
                }
                catch (Exception e) {
                    // Not worried about exception with closing
                }
            }
        }
        return success;
    }

    @Override
    public boolean reloadAccount(Wallet wallet) {
        synchronized (lock) {
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
                    wallet.setLockOut(rs.getBoolean("lockedOut"));
                }
                dCoBase.debug("Reloaded Wallet for: ".concat(wallet.getOwner()));
            }
            catch (SQLException sqlex) {
                dCoBase.severe("SQL Exception while reloading Wallet for: " + wallet.getOwner());
                dCoBase.stacktrace(sqlex);
                success = false;
            }
            catch (AccountingException aex) {
                dCoBase.severe("Accounting Exception while reloading Wallet for: " + wallet.getOwner());
                dCoBase.stacktrace(aex);
                success = false;
            }
            finally {
                try {
                    if (!WalletSQLiteSource.class.isInstance(this)) {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                        if (ps != null && !ps.isClosed()) {
                            ps.close();
                        }
                    }
                }
                catch (Exception e) {
                }
            }
            return success;
        }
    }
}
