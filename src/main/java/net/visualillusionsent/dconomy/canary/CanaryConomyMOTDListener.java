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
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.canary;

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.motd.MOTDKey;
import net.canarymod.motd.MessageOfTheDayListener;
import net.visualillusionsent.dconomy.accounting.AccountNotFoundException;
import net.visualillusionsent.dconomy.accounting.AccountingException;
import net.visualillusionsent.dconomy.api.account.wallet.WalletAPIListener;

import java.text.MessageFormat;

/**
 * Canary dConomy Message Of The Day Listener
 *
 * @author Jason (darkdiplomat)
 */
public final class CanaryConomyMOTDListener implements MessageOfTheDayListener {

    public CanaryConomyMOTDListener(CanaryConomy cdConomy) {
        Canary.motd().registerMOTDListener(this, cdConomy, false);
    }

    @MOTDKey(key = "{wallet.balance}")
    public String wallet_balance(MessageReceiver msgrec) {
        try {
            return MessageFormat.format("{0,number,0.00}", WalletAPIListener.walletBalance(msgrec.getName(), msgrec.hasPermission("dconomy.wallet.base")));
        }
        catch (AccountingException e) {
            return "no wallet access";
        }
        catch (AccountNotFoundException e) {
            return "no wallet access";
        }
    }
}
