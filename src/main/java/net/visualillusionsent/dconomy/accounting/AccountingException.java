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
package net.visualillusionsent.dconomy.accounting;

import net.visualillusionsent.dconomy.MessageTranslator;

/**
 * Accounting Exception<br>
 * Thrown when Accounting encounters a problem such as max balance or negative numbers
 *
 * @author Jason (darkdiplomat)
 */
public final class AccountingException extends RuntimeException {

    private final Object[] args;

    public AccountingException(String msg, Object... args) {
        super(msg);
        this.args = args;
    }

    public AccountingException(String msg, Throwable cause, Object... args) {
        super(msg, cause);
        this.args = args;
    }

    public AccountingException(Throwable cause, Object... args) {
        super(cause);
        this.args = args;
    }

    public final String getMessage() {
        return MessageTranslator.translate(super.getMessage(), "en_US", args);
    }

    public final String getLocalizedMessage(String locale) {
        return MessageTranslator.translate(super.getMessage(), locale, args);
    }

    private static final long serialVersionUID = 311216012013L;
}
