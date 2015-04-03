/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2015 Visual Illusions Entertainment
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

import net.visualillusionsent.dconomy.accounting.wallet.Wallet;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.dCoBase;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class WalletMySQLSource extends WalletSQLSource {

    public WalletMySQLSource(WalletHandler wallet_handler) {
        super(wallet_handler);
    }

    @Override
    public final boolean load() {
        try {
            testConnection();
            dCoBase.debug("Testing Wallet table and creating if needed...");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `" + wallet_table + "` " + //
                    "(`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL, `lockedOut` TINYINT(1) NOT NULL, PRIMARY KEY (`owner`))");
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
    public final boolean saveAccount(Wallet wallet) {
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
    public final boolean reloadAccount(Wallet wallet) {
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

    private void testConnection() throws SQLException {
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            conn = DriverManager.getConnection("jdbc:mysql://" + dCoBase.getProperties().getString("sql.database.url"), dCoBase.getProperties().getString("sql.user"), dCoBase.getProperties().getString("sql.password"));
        }
    }
}
