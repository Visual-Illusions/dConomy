package net.visualillusionsent.dconomy;

/**
 * dConomy User handling class
 * 
 * @since   2.0
 * @author  darkdiplomat
 *          <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 */
public class User {
    private String name;
    private boolean money, bank, joint, rank, create, auto, admin;
    
    /**
     * Class constructor.
     * 
     * @param name      The name of the user.
     * @param money     Boolean value for if user can use money commands.
     * @param bank      Boolean value for if user can use bank commands.
     * @param joint     Boolean value for if user can use joint commands.
     * @param rank      Boolean value for if user can use rank commands.
     * @param create    Boolean value for if user can create joint accounts.
     * @param auto      Boolean value for if user can use pay forwarding.
     * @param admin     Boolean value for if user is a dConomy Admin.
     * @since   2.0
     */
    public User(String name, boolean money, boolean bank, boolean joint, boolean rank, boolean create, boolean auto, boolean admin){
        this.name = name;
        this.money = money;
        this.bank = bank;
        this.joint = joint;
        this.rank = rank;
        this.create = create;
        this.auto = auto;
        this.admin = admin;
    }
    
    /**
     * Return the user's name
     * 
     * @return name     The name of this user.
     * @since   2.0
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return money    Boolean value for if user can use money commands.
     * @since   2.0
     */
    public boolean useMoney(){
        return money;
    }
    
    /**
     * Returns if a user can use bank commands
     * 
     * @return bank     Boolean value for if user can use bank commands.
     * @since   2.0
     */
    public boolean useBank(){
        return bank;
    }
    
    /**
     * Returns if a user can use joint commands
     * 
     * @return joint    Boolean value for if user can user joint commands.
     * @since   2.0
     */
    public boolean useJoint(){
        return joint;
    }
    
    /**
     * Returns if a user can use rank commands
     * 
     * @return rank     Boolean value for if use can use rank commands.
     * @since   2.0
     */
    public boolean canRank(){
        return rank;
    }
    
    /**
     * Returns if a user can create joint accounts
     * 
     * @return create   Boolean value for if user can create joint accounts.
     * @since   2.0
     */
    public boolean canCreate(){
        return create;
    }
    
    /**
     * Returns if a user can use money commands
     * 
     * @return auto     Boolean value for if user can use pay forwarding.
     * @since   2.0
     */
    public boolean canForward(){
        return auto;
    }
    
    /**
     * Returns if a user is a dConomy Admin
     * 
     * @return admin    Boolean value for if user is a dConomy Admin.
     * @since   2.0
     */
    public boolean isAdmin(){
        return admin;
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
