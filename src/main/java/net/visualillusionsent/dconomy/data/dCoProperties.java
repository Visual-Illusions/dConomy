/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
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
package net.visualillusionsent.dconomy.data;

import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;

public final class dCoProperties {

    private final PropertiesFile propsFile;
    private final String configDir;

    public dCoProperties() {
        configDir = "config/dConomy3/";

        File dir = new File(configDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File real = new File(configDir.concat("settings.cfg"));
        if (!real.exists()) {
            FileUtils.cloneFileFromJar(JarUtils.getJarPath(getClass()), "resources/default_config.cfg", configDir.concat("settings.cfg"));
        }
        propsFile = new PropertiesFile(configDir.concat("settings.cfg"));
        testProperties();
    }

    public final void reloadProperties() {
        propsFile.reload();
    }

    public final String getString(String key) {
        return propsFile.getString(key);
    }

    public final double getDouble(String key) {
        return propsFile.getDouble(key);
    }

    public final boolean getBooleanValue(String key) {
        return propsFile.getBoolean(key);
    }

    public final void setServerBalance(double value) {
        propsFile.setDouble("server.balance", value);
        propsFile.save();
    }

    public final void setServerAccountLock(boolean locked) {
        propsFile.setString("server.account.locked", locked ? "yes" : "no");
    }

    public final String getConfigurationDirectory() {
        return configDir;
    }

    private void testProperties() {
        propsFile.getDouble("default.balance", 0);
        propsFile.setComments("default.balance", "New Account Starting Balance value in 0.00 format (if set to less than 0.01 will default to 0");
        if (propsFile.getDouble("default.balance") < 0.01) {
            propsFile.setDouble("default.balance", 0);
        }

        propsFile.getDouble("max.account.balance", 999999999999999999D);
        propsFile.setComments("max.account.balance", "Max allow Account balance, should never be set to more than 999999999999999999");
        if (propsFile.getDouble("max.account.balance") > 999999999999999999D) {
            propsFile.setDouble("max.account.balance", 999999999999999999D);
        }

        propsFile.getString("money.name", "Voin$");
        propsFile.setComments("money.name", "Name to call your currency");

        propsFile.getString("server.max.always", "yes");
        propsFile.setComments("server.max.always", "Server always has maximum currency");

        propsFile.getString("datasource", "xml");
        propsFile.setComments("datasource", "Datasource Setting (case insensitive) - Types: xml, mysql, sqlite  (Required libraries not included in Server Mod will auto-download from an appropriate Maven Repository. If you have any concerns with this, please contact Visual Illusions: viadmin@visualillusionsent.net )");

        propsFile.getString("sql.user", "user");
        propsFile.setComments("sql.user", "MySQL Username");

        propsFile.getString("sql.password", "pass");
        propsFile.setComments("sql.password", "MySQL Password");

        propsFile.getString("sql.database.url", "localhost:3306/minecraft");
        propsFile.setComments("sql.database.url", "SQL Database URL (jdbc:[sql]: not required) Defaults: (SQLite) plugins/dConomy3/dConomy3.db (Others) domain.suffix:port/databasename");

        propsFile.getString("sql.wallet.table", "dCoWallet");
        propsFile.setComments("sql.wallet.table", "SQL Wallet table");

        propsFile.getString("adminonly.balance.check", "no");
        propsFile.setComments("adminonly.balance.check", "Admin Only Check Another Players Balance - Set to false to allow all");

        propsFile.getString("server.locale", "en_US");
        propsFile.setComments("server.locale", "The default language (CanaryMod server players will see messages in their locale, this will only effect the console output)");

        propsFile.save();
    }
}
