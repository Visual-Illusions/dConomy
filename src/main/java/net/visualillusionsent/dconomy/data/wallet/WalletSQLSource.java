/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *
 *     3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.visualillusionsent.dconomy.data.wallet;

import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.UserWallet;
import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.dconomy.data.DataLock;
import net.visualillusionsent.minecraft.plugin.util.Tools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class WalletSQLSource extends WalletDataSource {
    private final DataLock lock = new DataLock();
    protected Connection conn;
    protected String wallet_table = dCoBase.getProperties().getString("sql.wallet.table");

    public WalletSQLSource(WalletHandler wallet_handler) {
        super(wallet_handler);
    }

    @Override
    public boolean load() {
        boolean success = true;
        synchronized (lock) {
            HashMap<String, UUID> queuedUpdate = new HashMap<String, UUID>();
            PreparedStatement ps = null;
            ResultSet rs = null;
            int load = 0;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "`");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String owner = rs.getString("owner");
                    UUID ownerUUID;
                    if (Tools.isUserName(owner)) {
                        ownerUUID = dCoBase.getServer().getUUIDFromName(owner);
                        queuedUpdate.put(owner, ownerUUID);
                    }
                    else {
                        ownerUUID = UUID.fromString(owner);
                    }

                    double balance = rs.getDouble("balance");
                    boolean locked = rs.getBoolean("lockedOut");
                    wallet_handler.addWallet(new UserWallet(ownerUUID, balance, locked, this));
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
                    //
                }
            }
            // I have no clue if this will work...
            if (!queuedUpdate.isEmpty()) {
                try {
                    ps = conn.prepareStatement("UPDATE `" + wallet_table + "` SET `owner`=? WHERE `owner`=?");
                    for (Map.Entry<String, UUID> entry : queuedUpdate.entrySet()) {
                        ps.setString(1, entry.getValue().toString());
                        ps.setString(2, entry.getKey());
                        ps.addBatch();
                    }
                    ps.execute();
                }
                catch (SQLException sqlex) {
                    dCoBase.severe("Failed to update owners of Wallets... Old data and duplicates are likely to have occured...");
                }
            }
        }
        return success;
    }

    @Override
    public boolean saveAccount(Wallet wallet) {
        boolean success = true;
        synchronized (lock) {
            dCoBase.debug("Saving Wallet for: ".concat(wallet.getOwner().toString()));
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "` WHERE `owner`=?");
                ps.setString(1, wallet.getOwner().toString());
                rs = ps.executeQuery();
                boolean found = rs.next();
                if (found) {
                    ps.close();
                    ps = conn.prepareStatement("UPDATE `" + wallet_table + "` SET `balance`=?, `lockedOut`=? WHERE `owner`=?");
                    ps.setDouble(1, wallet.getBalance());
                    ps.setInt(2, wallet.isLocked() ? 1 : 0);
                    ps.setString(3, wallet.getOwner().toString());
                    ps.execute();
                }
                else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO `" + wallet_table + "` (`owner`,`balance`,`lockedOut`) VALUES(?,?,?)");
                    ps.setString(1, wallet.getOwner().toString());
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
            dCoBase.debug("Reloading Wallet for: ".concat(wallet.getOwner().toString()));
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = conn.prepareStatement("SELECT * FROM `" + wallet_table + "` WHERE `owner`=?");
                ps.setString(1, wallet.getOwner().toString());
                rs = ps.executeQuery();
                while (rs.next()) {
                    wallet.setBalance(rs.getDouble("balance"));
                    wallet.setLockOut(rs.getBoolean("lockedOut"));
                }
                dCoBase.debug("Reloaded Wallet for: ".concat(wallet.getOwner().toString()));
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
