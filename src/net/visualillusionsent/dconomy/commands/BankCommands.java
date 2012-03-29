package net.visualillusionsent.dconomy.commands;

import java.util.Map;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.messages.*;

/**
 * dConomy bank commands
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public enum BankCommands {
    
    /**
     * Checks account balance of user or another user if allowed.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * The command arguments: Either the user name to check account for or "" to check own account
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    BASE{
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
     * Withdraw specified amount from user's bank account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The amount to be withdrawn.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    WITHDRAW{
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
                return res;
            }
            if (withdraw < 0.01){ 
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.BANK, user.getName()) - withdraw;
            if (newBank < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E116.Mess(null)});
                return res;
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
     * Deposits specified amount into user's bank account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * Command Arguments: The amount to be deposited.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    DEPOSIT{
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
            if (deposit < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) - deposit;
            if (newAcc < 0){ 
                res.setMess(new String[]{prefix+ErrorMessages.E115.Mess(null)});
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
     * Resets specified user's bank account is user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The user to reset.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    RESET{
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
     * Sets specified user's bank account to specified amount if user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The amount to set and user of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    SET{
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
            if (balance < 0.01 && balance != 0){
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
     * Adds specified amount to specified user's bank account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The amount to add and user of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    ADD{
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
     * Removes specified amount to specified user's bank account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The amount to remove and user of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    REMOVE{
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
            if(newbal < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.BANK, user.getName(), newbal);
            res.setMess(new String[]{prefix+AdminMessages.A303.Mess(args[0], "Bank", newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    /**
     * Checks user ranking.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: The user to check rank of if applicable.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
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
                            log(LoggingMessages.L638.Mess(user.getName(), null, 0, null));
                            break;
                        }
                    } else {
                        if (name.equalsIgnoreCase(args[0])) {
                            ranked = AccountMessages.A221.Mess(args[0], null, 0, rank);
                            log(LoggingMessages.L638.Mess(user.getName(), args[0], 0, null));
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
    
    /**
     * Checks the top accounts.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: Amount to check if applicable.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
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
            
            log(LoggingMessages.L614.Mess(user.getName(), null, amount, null));
            
            res.setMess(ranking);
            return res;
        }
    },
    
    /**
     * Displays help information.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link BankCommands}
     * <p>
     * Command Arguments: none needed
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    HELP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            res.setMess(new String[]{HelpMessages.H402.Mess(),
                                     HelpMessages.H404.Mess(),
                                     HelpMessages.H405.Mess(),
                                     HelpMessages.H410.Mess(),
                                     HelpMessages.H417.Mess(),
                                     HelpMessages.H418.Mess(),
                                     (user.canRank() ? HelpMessages.H412.Mess() : null),
                                     (user.canRank() ? HelpMessages.H413.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H431.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H432.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H433.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H434.Mess() : null),
                                     (user.useMoney() ? HelpMessages.H406.Mess() : null),
                                     (user.useJoint() ? HelpMessages.H408.Mess() : null)
                                     });
            return res;
        }
    };
    
    private BankCommands(){ }
    
    private static final String prefix = "\u00A72[\u00A7adCo \u00A7fBank\u00A72] ";
    
    /**
     * Checks if proper amount of arguments were given
     * 
     * @param length
     * @param args
     * @return true if enough arguments are present.
     * @since 2.0
     */
    private static boolean argcheck(int length, String[] args){
        return args.length >= length;
    }
    
    /**
     * Used to log transactions if logging is enabled
     * 
     * @param message The logging message.
     * @since 2.0
     */
    private static void log(String message){
        DCoProperties.getDS().logTrans(message);
    }
    
    /**
     * Method for executing commands (overridden by the command enum)
     * 
     * @param user The user calling the command.
     * @param args The arguments for the command.
     * @return The ActionResult containing the messages to be sent.
     * @since 2.0
     */
    public ActionResult execute(User user, String[] args){
        return new ActionResult();
    }
}
