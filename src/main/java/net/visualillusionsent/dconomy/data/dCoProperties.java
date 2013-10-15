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
package net.visualillusionsent.dconomy.data;

import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

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
            FileUtils.cloneFileFromJar(getJarPath(), "resources/default_config.cfg", configDir.concat("settings.cfg"));
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

    public final PropertiesFile getPropertiesFile() {
        return propsFile;
    }

    private final String getJarPath() { // For when the jar isn't dConomy3.jar
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return codeSource.getLocation().toURI().getPath();
        }
        catch (URISyntaxException ex) {
        }
        return "plugins/dConomy3.jar";
    }

    public final String getConfigurationDirectory() {
        return configDir;
    }

    private final void testProperties() {
        boolean missingProp = false;
        if (!propsFile.containsKey("default.balance")) {
            propsFile.setDouble("default.balance", 0, "New Account Starting Balance value in 0.00 format (if set to less than 0.01 will default to 0");
            missingProp = true;
        }
        else if (propsFile.getDouble("default.balance") < 0.01) {
            propsFile.setDouble("default.balance", 0);
        }
        if (!propsFile.containsKey("max.account.balance")) {
            propsFile.setDouble("max.account.balance", 999999999999999999D, "Max allow Account balance, should never be set to more than 999999999999999999");
            missingProp = true;
        }
        else if (propsFile.getDouble("max.account.balance") > 999999999999999999D) {
            propsFile.setDouble("max.account.balance", 999999999999999999D);
        }
        if (!propsFile.containsKey("money.name")) {
            propsFile.setString("money.name", "Coins", "Name to call your currency");
            missingProp = true;
        }
        if (!propsFile.containsKey("server.max.always")) {
            propsFile.setString("server.max.always", "yes", "Server always has maximum currency");
            missingProp = true;
        }
        if (!propsFile.containsKey("datasource")) {
            propsFile.setString("datasource", "xml", "Datasource Setting (case insensitive) - Types: xml (jdom2.jar required for Bukkit), mysql, sqlite (sqlite.jar required for Canary)");
            missingProp = true;
        }
        if (!propsFile.containsKey("sql.user")) {
            propsFile.setString("sql.user", "user", "SQL Username");
            missingProp = true;
        }
        if (!propsFile.containsKey("sql.password")) {
            propsFile.setString("sql.password", "pass", "SQL Password");
            missingProp = true;
        }
        if (!propsFile.containsKey("sql.database.url")) {
            propsFile.setString("sql.database.url", "localhost:3306/minecraft", "SQL Database URL (jdbc:[sql]: not required) Defaults: (SQLite) plugins/dConomy3/dConomy3.db (Others) domain.suffix:port/databasename");
            missingProp = true;
        }
        if (!propsFile.containsKey("sql.wallet.table")) {
            propsFile.setString("sql.wallet.table", "dCoWallet", "SQL Wallet table");
            missingProp = true;
        }
        if (!propsFile.containsKey("adminonly.balance.check")) {
            propsFile.setString("adminonly.balance.check", "no", "Admin Only Check Another Players Balance - Set to false to allow all");
            missingProp = true;
        }
        if (!propsFile.containsKey("debug.enabled")) {
            propsFile.setString("debug.enabled", "no", "Debugging");
            missingProp = true;
        }
        if (missingProp) {
            propsFile.save();
        }
    }
}
