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
