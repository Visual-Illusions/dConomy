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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting;


/**
 * Accounting Exception<br>
 * Thrown when Accounting encounters a problem such as max balance or negative numbers
 * 
 * @author Jason (darkdiplomat)
 * 
 */
public final class AccountingException extends RuntimeException{

    public AccountingException(String msg){
        super(msg);
    }

    public AccountingException(String msg, Throwable cause){
        super(msg, cause);
    }

    public AccountingException(Throwable cause){
        super(cause);
    }

    public final String getMessage(){
        return super.getMessage();
    }

    private static final long serialVersionUID = 311216012013L;
}
