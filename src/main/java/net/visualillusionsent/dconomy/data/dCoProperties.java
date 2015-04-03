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
package net.visualillusionsent.dconomy.data;

import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;
import java.io.IOException;

public final class dCoProperties {

    private final PropertiesFile propsFile;

    public dCoProperties() {
        String configDirOld = "config/dConomy3/";

        File dir = new File(getConfigurationDirectory());

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new PluginInitializationException("Unable to create directories for the dConomy configuration");
            }
        }

        File real = new File(dir, "settings.cfg");
        File dirOld = new File(configDirOld);
        if (!real.exists()) {
            if (dirOld.exists()) {
                File set = new File(dirOld, "settings.cfg");
                if (!set.renameTo(real)) {
                    dCoBase.warning("Failed to migrate old settings file.");
                }
                File wallets = new File(dir, "wallets.xml");
                if (!wallets.exists()) {
                    File oldWallets = new File(dirOld, "wallets.xml");
                    if (!oldWallets.renameTo(wallets)) {
                        dCoBase.warning("Failed to migrate old wallets file");
                    }
                }
            }
            else {
                try {
                    FileUtils.cloneFileFromJar(JarUtils.getJarPath(getClass()), "resources/default_config.cfg", getConfigurationDirectory().concat("settings.cfg"));
                }
                catch (IOException e) {
                    // Well that didnt work...
                }
            }
        }

        propsFile = new PropertiesFile(getConfigurationDirectory().concat("settings.cfg"));
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
        return "config/dConomy/";
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

        propsFile.getBoolean("update.lang", true);
        propsFile.setComments("update.lang", "Whether to auto-update lang files");

        propsFile.save();
    }
}
