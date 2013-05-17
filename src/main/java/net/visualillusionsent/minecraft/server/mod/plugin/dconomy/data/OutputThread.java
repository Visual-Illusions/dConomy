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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.Account;

final class OutputThread extends Thread{

    private final dCoDataHandler handler;
    private volatile boolean running = true;

    public OutputThread(dCoDataHandler handler){
        this.handler = handler;
    }

    public void run(){
        while (running) {
            Account account = null;
            try {
                account = handler.getQueue().next();
                if (account.getDataSource() != null) {
                    account.getDataSource().saveAccount(account);
                }
            }
            catch (Exception ex) {
                dCoBase.severe("Exception occurred in OutputThread for Account: " + (account != null ? account.getClass().getSimpleName() : "UNKNOWN ACCOUNT CLASS") + ":" + account.getOwner());
                dCoBase.stacktrace(ex);
            }
        }
    }

    public final void terminate(){
        running = false;
        interrupt();
    }
}
