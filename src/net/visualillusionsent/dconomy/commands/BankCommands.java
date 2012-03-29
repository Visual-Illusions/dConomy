package net.visualillusionsent.dconomy.commands;

import java.util.Map;

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
        /**
         * Checks account balance of user or another user if allowed.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments. Either the user name to check account for or "" to check own account
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!args[0].equals("") && !DCoProperties.getAOC()){
                if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                    res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                    return res; 
                }
                res.setMess(new String[]{prefix+AccountMessages.A203.Mess(args[0], "Bank", DCoProperties.getDS().getBalance(AccountType.BANK, args[0]), -1)});
                return res;
            }
            else{
                res.setMess(new String[]{prefix+AccountMessages.A201.Mess(null, "Bank", DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()), -1)});
                return res;
            }
        }
    },
    
    /**
     * bank withdraw command    
     *      Allows user to withdraw money from bank account.
     */
    WITHDRAW{
        /**
         * Withdraw specified amount from user's bank account.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of the amount to be withdrawn.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(1, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            double withdraw = 0;
            try{
                withdraw = Double.parseDouble(args[0]);
            }
            catch(NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
            }
            if (withdraw < 0.01 && withdraw != 0){ 
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) - withdraw;
            if (newBank < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) + withdraw;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newBank);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+AccountMessages.A205.Mess(null, null, withdraw, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Account", newAcc, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Bank", newBank, -1)});
            log(LoggingMessages.L603.Mess(user.getName(), null, withdraw, null));
            return res;
        }
    },
    
    /**
     * bank deposit command     
     *      Allows user to deposit money into bank account.
     */
    DEPOSIT{
        /**
         * Deposits specified amount into user's bank account.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of the amount to be deposited.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(1, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            double deposit = 0;
            try{
                deposit = Double.parseDouble(args[0]);
            }
            catch(NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (deposit < 0.01 && deposit != 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) - deposit;
            if (newAcc < 0){ 
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) + deposit;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newBank);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+AccountMessages.A204.Mess(null, null, deposit, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Account", newAcc, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Bank", newBank, -1)});
            log(LoggingMessages.L602.Mess(user.getName(), null, deposit, null));
            return res;
        }
    },
    
    /**
     * bank reset command
     *      Allows a dConomy admin to reset a user's bank account.
     */
    RESET{
        /**
         * Resets specified user's bank account is user calling command is admin.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of user to reset.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.BANK, args[0], 0);
            res.setMess(new String[]{prefix+AdminMessages.A301.Mess(args[0], "Account", 0)});
            log(LoggingMessages.L622.Mess(user.getName(), args[0], 0, null));
            return res;
        }
    },
    
    /**
     * bank set command
     *      Allows a dConomy admin to set a user's bank account to a specified amount.
     */
    SET{
        /**
         * Sets specified user's bank account to specified amount if user calling command is admin.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of the amount and user.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            double balance = 0;
            try{
                balance = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (balance < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.BANK, args[0], balance);
            res.setMess(new String[]{prefix+AdminMessages.A302.Mess(args[0], "Bank", balance)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], balance, null));
            return res;
        }
    },
    
    /**
     * bank add command
     *      Allows a dConomy admin to add a specified amount to a user's bank account.
     */
    ADD{
        /**
         * Adds specified amount to specified user's bank account
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of the amount and user.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            double deposit = 0;
            try{
                deposit = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (deposit < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newbal = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) + deposit;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newbal);
            res.setMess(new String[]{prefix+AdminMessages.A304.Mess(args[0], "Bank", newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    /**
     * bank remove command
     *      Allows a dConomy admin to remove a specified amount to a user's bank account.
     */
    REMOVE{
        /**
         * Removes specified amount to specified user's bank account
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of the amount and user.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            double deduct = 0;
            try{
                deduct = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (deduct < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newbal = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) - deduct;
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newbal);
            res.setMess(new String[]{prefix+AdminMessages.A303.Mess(args[0], "Bank", newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    RANK{
        public ActionResult execute(User user, String[] args){
            int rank = -1;
            ActionResult res = new ActionResult();
            if(!user.canRank()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            boolean self = true;
            if(!args[0].equals("")){
                if(!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                    res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                    return res;
                }
                self = false;
            }
            
            String ranked = null;
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.BANK);
            if(sorted != null){
                for (String name : sorted.keySet()) {
                    if (self) {
                        if (name.equalsIgnoreCase(user.getName())) {
                            ranked = AccountMessages.A222.Mess(null, null, 0, rank);
                            //dCD.Logging(638, player, "", "", "");
                            break;
                        }
                    } else {
                        if (name.equalsIgnoreCase(args[0])) {
                            ranked = AccountMessages.A221.Mess(args[0], null, 0, rank);
                            //dCD.Logging(638, player, Other, "", "");
                            break;
                        }
                    }
                    rank++;
                }
            }
            
            res.setMess(new String[]{ ranked != null ? prefix+rank : prefix+"Unable to retrieve rank..." });
            return res;
        }
    },
    
    TOP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            
            if(!user.canRank()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            
            int rank = 1, amount = 20;
            
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.BANK);
            
            if (sorted.size() < 1) {
                res.setMess(new String[]{ AccountMessages.A223.Mess(null, "Bank", 0, 0)});
                return res;
            }

            if(!args[0].equals("")){
                try{
                    amount = Integer.parseInt(args[0]);
                }
                catch(NumberFormatException nfe){
                    amount = 20;
                }
            }
            
            if (amount > sorted.size()) {
                amount = sorted.size();
            }
            
            String[] ranking = new String[amount+1];
            ranking[0] = AccountMessages.A223.Mess(null, "Bank", 0, amount);
            
            for(String name : sorted.keySet()) {
                double balance = sorted.get(name);

                if (rank <= amount) {
                    ranking[rank] = AccountMessages.A224.Mess(name, null, balance, rank);
                }
                else{
                    break;
                }
                rank++;
            }
            
            //dCD.Logging(614, player, "", String.valueOf(amount), "");
            
            res.setMess(ranking);
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
            res.setMess(new String[]{HelpMessages.H515.Mess(),
                                     HelpMessages.H540.Mess(),
                                     HelpMessages.H516.Mess(),
                                     HelpMessages.H517.Mess(),
                                     HelpMessages.H518.Mess(),
                                     (user.isAdmin() ? HelpMessages.H542.Mess() : null) });
            return res;
        }
    };
    
    private BankCommands(){ }
    
    private static final String prefix = "\u00A72[\u00A7adCo \u00A7fBank\u00A72] ";
    
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
