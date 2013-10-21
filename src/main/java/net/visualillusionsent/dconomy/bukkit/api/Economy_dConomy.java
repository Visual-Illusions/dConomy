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
package net.visualillusionsent.dconomy.bukkit.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAPIListener;
import net.visualillusionsent.dconomy.bukkit.BukkitConomy;
import net.visualillusionsent.dconomy.dCoBase;
import org.bukkit.plugin.Plugin;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * The Vault dConomy service
 *
 * @author Jason (darkdiplomat)
 */
public class Economy_dConomy implements Economy {
    private static final Logger log = Logger.getLogger("Minecraft");
    private final String name = "dConomy ";
    private Plugin plugin; // The Vault plugin reference
    protected BukkitConomy economy; // The dConomy reference

    public Economy_dConomy(Plugin plugin) {
        this.plugin = plugin;
        //Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(this), plugin); // This would most likely fail if we tried implementing it inside of dConomy itself

        if (economy == null) {
            Plugin econ = plugin.getServer().getPluginManager().getPlugin("dConomy");
            if (econ != null && econ.isEnabled()) {
                this.economy = (BukkitConomy) econ;
                log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), name));
            }
        }
    }

    /* // This would most likely fail if we tried implementing it inside of dConomy itself
    public class EconomyServerListener implements Listener {
        Economy_dConomy economy = null;

        public EconomyServerListener(Economy_dConomy economy) {
            this.economy = economy;
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (economy.economy == null) {
                Plugin ec = event.getPlugin();

                if (ec.getDescription().getName().equals("dConomy") && ec.getClass().getName().equals("net.visualillusionsent.dconomy.bukkit.BukkitConomy")) {
                    economy.economy = (BukkitConomy) ec;
                    log.info(String.format("[%s][Economy] %s hooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (economy.economy != null) {
                if (event.getPlugin().getDescription().getName().equals("dConomy")) {
                    economy.economy = null;
                    log.info(String.format("[%s][Economy] %s unhooked.", plugin.getDescription().getName(), economy.name));
                }
            }
        }
    }
    */

    @Override
    public boolean isEnabled() {
        if (economy == null) {
            return false;
        }
        else {
            return economy.isEnabled();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String format(double amount) {
        return MessageFormat.format("{0,number,0.00}", amount);
    }

    @Override
    public String currencyNameSingular() {
        return dCoBase.getMoneyName();
    }

    @Override
    public String currencyNamePlural() {
        return dCoBase.getMoneyName();
    }

    @Override
    public double getBalance(String playerName) {
        try {
            return WalletAPIListener.getBalance(playerName, true);
        }
        catch (Exception ex) {
            return -1; // This probably won't happen
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        try {
            return new EconomyResponse(amount, WalletAPIListener.walletDebit("Vault", playerName, amount, true), ResponseType.SUCCESS, null);
        }
        catch (Exception ex) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, ex.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        try {
            return new EconomyResponse(amount, WalletAPIListener.walletDeposit("Vault", playerName, amount, true), ResponseType.SUCCESS, null);
        }
        catch (Exception ex) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, ex.getMessage());
        }
    }

    @Override
    public boolean has(String playerName, double amount) {
        try {
            WalletAPIListener.testWalletDebit(playerName, amount);
        }
        catch (AccountingException e) {
            return false;
        }
        return true;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "dConomy does not natively support Bank Accounts");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<String>();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return WalletHandler.verifyAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        try {
            WalletAPIListener.getBalance(playerName, true);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }
}
