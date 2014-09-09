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
package net.visualillusionsent.dconomy.bukkit.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
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
    private Plugin vault; // The Vault plugin reference
    protected BukkitConomy economy; // The dConomy reference

    public Economy_dConomy(Plugin plugin) {
        this.vault = plugin;
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
            return WalletAPIListener.walletBalance(playerName, true);
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
        try {
            WalletAPIListener.isLocked(playerName);
            return true;
        }
        catch (AccountNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        try {
            WalletAPIListener.walletBalance(playerName, true);
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
