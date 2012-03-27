package net.visualillusionsent.dconomy;

/**
 * ActionResult.java - dConomy Class for handling the sending of player messages
 * 
 * @since   2.0
 * @author  darkdiplomat
 *          <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 */
public class ActionResult {
    private String[] mess;
    private String[] othermess = null;
    private String otherreceiver = null;
    
    /**
     * Class constructor.
     * 
     * @since   2.0
     */
    public ActionResult(){
        mess = new String[]{""};
    }
    
    /**
     * Sets the result messages.
     * 
     * @param mess  String array of messages to be sent.
     * @since   2.0
     */
    public void setMess(String[] mess){
        this.mess = mess;
    }
    
    /**
     * Sets the result other messages.
     * 
     * @param receiver  The receivers name.
     * @param mess      String array of the messages to be sent.
     * @since   2.0
     */
    public void setOtherMess(String receiver, String[] mess){
        this.otherreceiver = receiver;
        this.othermess = mess;
    }
    
    /**
     * Gets the messages to be sent.
     * 
     * @return mess     String array of messages to be sent.
     * @since   2.0
     */
    public String[] getMess(){
        return mess;
    }
    
    /**
     * Gets the receiver for the other messages.
     * 
     * @return otherreceiver    The name of the other receiver.
     * @since   2.0
     */
    public String getOtherReceiver(){
        return otherreceiver;
    }
    
    /**
     * Gets the other messages to be sent
     * 
     * @return othermess    String array of the other messages.
     * @since   2.0
     */
    public String[] getOtherMess(){
        return othermess;
    }
}

/*******************************************************************************\
* dConomy                                                                       *
* Copyright (C) 2011-2012 Visual Illusions Entertainment                        *
* Author: darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of dConomy.                                                 *                       
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
* GNU General Public License for more details.                                  *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.   *
\*******************************************************************************/
