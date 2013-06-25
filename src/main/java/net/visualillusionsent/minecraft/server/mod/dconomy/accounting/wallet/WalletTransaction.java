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
package net.visualillusionsent.minecraft.server.mod.dconomy.accounting.wallet;

import net.visualillusionsent.minecraft.server.mod.dconomy.accounting.AccountTransaction;
import net.visualillusionsent.minecraft.server.mod.interfaces.IModUser;

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

    private final IModUser sender, recipient;
    private final ActionType type;
    private final double amount;

    public WalletTransaction(IModUser sender, IModUser recipient, ActionType type, double amount){
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
        this.amount = amount;
    }

    public final IModUser getSender(){
        return sender;
    }

    public final IModUser getRecipient(){
        return recipient;
    }

    public final ActionType getActionType(){
        return type;
    }

    public final double getAmountChange(){
        return amount;
    }

}
