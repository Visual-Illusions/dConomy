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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.AccountTransaction;

public final class WalletTransaction extends AccountTransaction{
    public enum ActionType {
        PAY, //
        ADMIN_ADD, //
        ADMIN_REMOVE, //
        ADMIN_SET, //
        PLUGIN_DEBIT, //
        PLUGIN_DEPOSIT, //
        PLUGIN_SET, //
        ;
    }

    private final Mod_User sender, recipient;
    private final ActionType type;
    private final double amount;

    public WalletTransaction(Mod_User sender, Mod_User recipient, ActionType type, double amount){
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
    }

    public final Mod_User getSender(){
        return sender;
    }

    public final Mod_User getRecipient(){
        return recipient;
    }

    public final ActionType getActionType(){
        return type;
    }

    public final double getAmountChange(){
        return amount;
    }

}
