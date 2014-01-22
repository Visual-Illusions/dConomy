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
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.accounting;

/**
 * Thrown when an Account is not found
 *
 * @author Jason (darkdiplomat)
 */
public final class AccountNotFoundException extends Exception {
    private static final long serialVersionUID = 210106102013L;

    public AccountNotFoundException(String accountType, String userName) {
        super(String.format("'%s' for User: '%s' was not found", accountType, userName));
    }

}
