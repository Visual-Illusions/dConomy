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
package net.visualillusionsent.lang;

/**
 * Initialization Error<br>
 * thrown if something causes the program to no be able to initialize
 * 
 * @author Jason (darkdiplomat)
 */
public final class InitializationError extends Error{

    /**
     * Constructs a new Initialization Error with a message
     * 
     * @param msg
     *            the message to add
     */
    public InitializationError(String msg){
        super(msg);
    }

    /**
     * Constructs a new Initialization Error with a message and cause
     * 
     * @param msg
     *            the message to add
     * @param cause
     *            the {@link Throwable} cause for the error
     */
    public InitializationError(String msg, Throwable cause){
        super(msg, cause);
    }

    /**
     * Constructs a new Initialization Error with a cause
     * 
     * @param cause
     *            the {@link Throwable} cause for the error
     */
    public InitializationError(Throwable cause){
        super(cause);
    }

    private static final long serialVersionUID = 2772175982743369746L;
}
