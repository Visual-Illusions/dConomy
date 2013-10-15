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

import net.visualillusionsent.dconomy.accounting.Account;
import net.visualillusionsent.dconomy.dCoBase;

final class OutputThread extends Thread {

    private final dCoDataHandler handler;
    private volatile boolean running = true;

    public OutputThread(dCoDataHandler handler) {
        super("dConomy-OutputQueue-Thread");
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    public void run() {
        while (running) {
            Account account = null;
            try {
                account = handler.getQueue().next();
                if (account != null && account.getDataSource() != null) {
                    account.getDataSource().saveAccount(account);
                }
            }
            catch (Exception ex) {
                if (running) {
                    dCoBase.severe("Exception occurred in OutputThread for Account: " + (account != null ? account.getClass().getSimpleName() : "UNKNOWN ACCOUNT CLASS") + ":" + (account != null ? account.getOwner() : "UNKNOWN OWNER"));
                    dCoBase.stacktrace(ex);
                }
            }
        }
    }

    public final void terminate() {
        running = false;
        interrupt();
    }
}
