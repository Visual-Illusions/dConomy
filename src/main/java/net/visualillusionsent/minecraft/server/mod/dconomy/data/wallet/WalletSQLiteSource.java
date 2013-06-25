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
package net.visualillusionsent.minecraft.server.mod.dconomy.data.wallet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import net.visualillusionsent.minecraft.server.mod.dconomy.dCoBase;

public final class WalletSQLiteSource extends WalletSQLSource{
    private static WalletSQLiteSource $;
    private final String db_Path = dCoBase.getProperties().getString("sql.database.url");

    public WalletSQLiteSource(){
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
            st.execute("CREATE TABLE IF NOT EXISTS `" + wallet_table + "` (`owner` VARCHAR(16) NOT NULL, `balance` DOUBLE(18,2) NOT NULL, `lockedOut` TINYINT(1) NOT NULL, PRIMARY KEY (`owner`))");
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
