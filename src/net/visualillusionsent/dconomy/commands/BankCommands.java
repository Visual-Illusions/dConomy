package net.visualillusionsent.dconomy.commands;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.messages.*;

/**
 * dConomy bank commands
 * 
 * @since   2.0
 * @author  darkdiplomat
 *          <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 */
public enum BankCommands {
    
    /**
     * base bank command    
     *      Displays bank balances.
     */
    BASE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!args[0].equals("") && !DCoProperties.getAOC()){
                if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                    res.setMess(new String[]{prefix+ErrorMessages.E109.Mess(args[0])});
                    return res; 
                }
                res.setMess(new String[]{prefix+PlayerMessages.P223.Mess(args[0], null, DCoProperties.getDS().getBalance(AccountType.BANK, args[0]))});
                return res;
            }
            else{
                res.setMess(new String[]{prefix+PlayerMessages.P201.Mess(null, null, DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()))});
                return res;
            }
        }
    },
    
    /**
     * bank withdraw command    
     *      Allows user to withdraw money from bank account.
     */
    WITHDRAW{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(1, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E106.Mess(null)});
                return res;
            }
            double withdraw = 0;
            try{
                withdraw = Double.parseDouble(args[0]);
            }
            catch(NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
            }
            if (withdraw < 0.01 && withdraw != 0){ 
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) - withdraw;
            if (newBank < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) + withdraw;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newBank);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+PlayerMessages.P203.Mess(null, null, withdraw), 
                                     prefix+PlayerMessages.P206.Mess(null, null, newAcc), 
                                     prefix+PlayerMessages.P205.Mess(null, null, newBank)});
            log(LoggingMessages.L603.Mess(user.getName(), null, withdraw, null));
            return res;
        }
    },
    
    /**
     * bank deposit command     
     *      Allows user to deposit money into bank account.
     */
    DEPOSIT{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(1, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E106.Mess(null)});
                return res;
            }
            double deposit = 0;
            try{
                deposit = Double.parseDouble(args[0]);
            }catch(NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (deposit < 0.01 && deposit != 0){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) - deposit;
            if (newAcc < 0){ 
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) + deposit;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newBank);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+PlayerMessages.P204.Mess(null, null, deposit), 
                                     prefix+PlayerMessages.P206.Mess(null, null, newAcc), 
                                     prefix+PlayerMessages.P205.Mess(null, null, newBank)});
            log(LoggingMessages.L602.Mess(user.getName(), null, deposit, null));
            return res;
        }
    },
    
    /**
     * bank reset command
     *      Allows a dConomy admin to reset a user's bank account.
     */
    RESET{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E109.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.BANK, args[0], 0);
            res.setMess(new String[]{prefix+AdminMessages.A408.Mess(args[0], null, 0)});
            log(LoggingMessages.L622.Mess(user.getName(), args[0], 0, null));
            return res;
        }
    },
    
    /**
     * bank set command
     *      Allows a dConomy admin to set a user's bank account to a specified amount.
     */
    SET{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E106.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E109.Mess(null)});
                return res;
            }
            double balance = 0;
            try{
                balance = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (balance < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E126.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.BANK, args[0], balance);
            res.setMess(new String[]{prefix+AdminMessages.A409.Mess(args[0], null, balance)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], balance, null));
            return res;
        }
    },
    
    /**
     * bank add command
     *      Allows a dConomy admin to add a specified amount to a user's bank account.
     */
    ADD{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            return res;
        }
    },
    
    /**
     * bank remove command
     *      Allows a dConomy admin to remove a specified amount to a user's bank account.
     */
    REMOVE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            return res;
        }
    },
    
    /**
     * bank help command
     *      Displays the help for bank commands.
     */
    HELP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            return res;
        }
    };
    
    private BankCommands(){ }
    
    private static final String prefix = "\u00A72[\u00A7fdCo-Bank\u00A72]\u00A7f ";
    
    private static boolean argcheck(int length, String[] args){
        return args.length >= length;
    }
    
    private static void log(String message){
        DCoProperties.getDS().logTrans(message);
    }
    
    public ActionResult execute(User user, String[] args){
        return new ActionResult();
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
