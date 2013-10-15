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

import java.util.LinkedList;
import java.util.NoSuchElementException;

public final class OutputQueue {

    private LinkedList<Account> queue;

    public OutputQueue() {
        queue = new LinkedList<Account>();
    }

    public final void add(Account account) {
        synchronized (queue) {
            queue.add(account);
            queue.notify();
        }
    }

    public final Account next() {
        Account account = null;
        if (queue.isEmpty()) {
            synchronized (queue) {
                try {
                    queue.wait();
                }
                catch (InterruptedException ex) {
                    return null;
                }
            }
        }
        try {
            account = queue.getFirst();
            queue.removeFirst();
        }
        catch (NoSuchElementException nsee) {
            throw new InternalError("Race hazard in LinkedList object.");
        }
        return account;
    }

    public final void clear() {
        synchronized (queue) {
            queue.clear();
        }
    }

    public final boolean hasNext() {
        return queue.size() > 0;
    }
}
