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
package net.visualillusionsent.dconomy.data;

import net.visualillusionsent.dconomy.accounting.Account;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public final class dCoDataHandler {

    private final OutputQueue queue;
    private final OutputThread outThread;
    private final DataSourceType dataType;

    public dCoDataHandler(DataSourceType type) throws DataSourceException {
        dataType = type;
        if (type == DataSourceType.XML) {
            testJDOM();
        }
        else if (type == DataSourceType.MYSQL) {
            testMySQLDriver();
        }
        else if (type == DataSourceType.SQLITE) {
            testSQLiteDriver();
        }
        else {
            throw new DataSourceException();
        }
        queue = new OutputQueue();
        outThread = new OutputThread(this);
        outThread.start();
    }

    public void addToQueue(Account account) {
        queue.add(account);
    }

    OutputQueue getQueue() {
        return queue;
    }

    @SuppressWarnings("unchecked")
    public void cleanUp() {
        outThread.terminate();
        while (queue.hasNext()) { // Save the rest immediately
            Account account = queue.next();
            if (account != null && account.getDataSource() != null) {
                account.getDataSource().saveAccount(account);
            }
        }
    }

    public final DataSourceType getDataSourceType() {
        return dataType;
    }

    private void testJDOM() throws DataSourceException {
        try {
            Class.forName("org.jdom2.JDOMException");
        }
        catch (ClassNotFoundException cnfe) {
            throw new DataSourceException(cnfe, DataSourceType.XML);
        }
    }

    private boolean cannotFindSQLDriver(String driver) {
        Enumeration<Driver> en = DriverManager.getDrivers();
        while (en.hasMoreElements()) {
            Driver drive = en.nextElement();
            if (drive.getClass().getName().equals(driver)) {
                return false;
            }
        }
        return true;
    }

    private void testSQLiteDriver() throws DataSourceException {
        if (cannotFindSQLDriver("org.sqlite.JDBC")) {
            try {
                Class.forName("org.sqlite.JDBC");
            }
            catch (ClassNotFoundException cnfe) {
                throw new DataSourceException(cnfe, DataSourceType.SQLITE);
            }
        }
    }

    private void testMySQLDriver() throws DataSourceException {
        if (cannotFindSQLDriver("com.mysql.jdbc.Driver")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (ClassNotFoundException cnfe) {
                throw new DataSourceException(cnfe, DataSourceType.MYSQL);
            }
        }
    }
}
