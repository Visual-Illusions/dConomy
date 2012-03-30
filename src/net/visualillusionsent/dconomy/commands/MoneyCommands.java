package net.visualillusionsent.dconomy.commands;

import java.util.Map;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.messages.*;
import net.visualillusionsent.dconomy.data.DCoProperties;

/**
 * dConomy money commands
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public enum MoneyCommands {
    
    /**
     * Adds specified amount to specified user's account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The amount to add and user's name of the account.
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
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
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
            double newbal = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, args[0]) + deposit;
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], newbal);
            res.setMess(new String[]{prefix+AdminMessages.A304.Mess(args[0], "Account", deposit)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        } 
    },
    
    /**
     * Checks if user has Pay Forwarding set up
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: none needed
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    AUTO{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            
            if(!user.canForward()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            
            if (user.canForward() && DCoProperties.getDS().isPayForwarding(user.getName())){
                String account = DCoProperties.getDS().getPayForwardingAcc(user.getName());
                if (account.equals("Bank")){
                    res.setMess(new String[]{prefix+AccountMessages.A219.Mess(null, null, 0, -1)});
                }else{
                    res.setMess(new String[]{prefix+AccountMessages.A220.Mess(null, account, 0, -1)});
                }
            }
            else{
                res.setMess(new String[]{prefix+AccountMessages.A218.Mess(null, null, 0, -1)});
            }
            
            return res;
        }
    },
    
    /**
     * Checks account balance of user or another user if allowed.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * The command arguments: Either the user's name to check account for or "" to check own account
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    BASE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!args[0].equals("") && !DCoProperties.getAOC()){
                if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                    res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                    return res; 
                }
                res.setMess(new String[]{prefix+AccountMessages.A203.Mess(args[0], "Account", DCoProperties.getDS().getBalance(AccountType.ACCOUNT, args[0]), -1)});
                return res;
            }
            else if(!user.getName().equals("SERVER")){
                res.setMess(new String[]{prefix+AccountMessages.A201.Mess(null, "Account", DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()), -1)});
                return res;
            }
            else{
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
        }
    },
    
    /**
     * Displays help information.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: none needed
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    HELP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            res.setMess(new String[]{HelpMessages.H401.Mess(),
                                     HelpMessages.H404.Mess(),
                                     HelpMessages.H405.Mess(),
                                     HelpMessages.H409.Mess(),
                                     HelpMessages.H414.Mess(),
                                     (user.canRank() ? HelpMessages.H412.Mess() : null),
                                     (user.canRank() ? HelpMessages.H413.Mess() : null),
                                     (user.canForward() ? HelpMessages.H415.Mess() : null),
                                     (user.canForward() ? HelpMessages.H416.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H431.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H432.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H433.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H434.Mess() : null),
                                     (user.useBank() ? HelpMessages.H407.Mess() : null),
                                     (user.useJoint() ? HelpMessages.H408.Mess() : null)
                                     });
            return res;
        }
    },
    
    /**
     * Pays specified user specified amount.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The user's name of the account to pay and amount to pay.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    PAY{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            
            if (args[0].equals(user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E118.Mess(args[0])});
                return res;
            }
            
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                return res;
            }
            
            double change = 0;
            
            try{
                change = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            
            if (change < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            
            AccountType sendAT = AccountType.ACCOUNT, recAT = AccountType.ACCOUNT;
            String sendAcc = user.getName(), recAcc = args[0];
            if (DCoProperties.getDS().isPayForwarding(user.getName()) && user.canForward()){
                sendAcc = DCoProperties.getDS().getPayForwardingAcc(user.getName());
                if(sendAcc.matches("Bank")){
                    sendAT = AccountType.BANK;
                }
                else if(DCoProperties.getDS().AccountExists(AccountType.JOINT, sendAcc) && DCoProperties.getDS().isJointOwner(sendAcc, user.getName())){
                    if(DCoProperties.getDS().canWithdraw(user.getName(), sendAcc, change)){
                        DCoProperties.getDS().addJUWD(sendAcc, user.getName(), change);
                        sendAT = AccountType.JOINT;
                    }
                }
            }
            
            if (DCoProperties.getDS().isPayForwarding(args[0])){
                recAcc = DCoProperties.getDS().getPayForwardingAcc(args[0]);
                if(recAcc.equals("Bank")){
                    recAT = AccountType.BANK;
                }
                else if(DCoProperties.getDS().AccountExists(AccountType.JOINT, recAcc) && DCoProperties.getDS().isJointUser(recAcc, args[0])){
                    sendAT = AccountType.JOINT;
                }
            }
            
            double balanceSender = DCoProperties.getDS().getBalance(sendAT, sendAcc);
            double balanceReceiver = DCoProperties.getDS().getBalance(recAT, recAcc);
            double deduct = balanceSender - change;
            double deposit = balanceReceiver + change;
                
            if (deduct < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E115.Mess(null)});
                return res;
            }
              
            DCoProperties.getDS().setBalance(sendAT, sendAcc, deduct);
            DCoProperties.getDS().setBalance(recAT, recAcc, deposit);
            
            if(sendAT.equals(AccountType.ACCOUNT)){
                res.setMess(new String[]{prefix+AccountMessages.A208.Mess(args[0], null, change, -1),
                                         prefix+AccountMessages.A202.Mess(null, "Account", deduct, -1)});
                log(LoggingMessages.L601.Mess(user.getName(), args[0], change, null));
            }
            else if(sendAT.equals(AccountType.BANK)){
                res.setMess(new String[]{prefix+AccountMessages.A208.Mess(args[0], null, change, -1),
                                         prefix+AccountMessages.A210.Mess(null, "Bank", change, -1),
                                         prefix+AccountMessages.A202.Mess(null, "Bank", deduct, -1)});
                log(LoggingMessages.L609.Mess(user.getName(), args[0], change, null));
            }
            else{
                res.setMess(new String[]{prefix+AccountMessages.A208.Mess(args[0], null, change, -1),
                                         prefix+AccountMessages.A211.Mess(null, sendAcc, change, -1),
                                         prefix+AccountMessages.A235.Mess(null, sendAcc, deduct, -1)});
                log(LoggingMessages.L610.Mess(user.getName(), args[0], change, sendAcc));
            }
            
            if(recAT.equals(AccountType.ACCOUNT)){
                res.setOtherMess(args[0], new String[]{prefix+AccountMessages.A212.Mess(user.getName(), null, change, -1),
                                                       prefix+AccountMessages.A202.Mess(null, "Account", deposit, -1)});
            }
            else if(recAT.equals(AccountType.BANK)){
                res.setOtherMess(args[0], new String[]{prefix+AccountMessages.A212.Mess(user.getName(), null, change, -1),
                                                       prefix+AccountMessages.A213.Mess(null, null, deposit, -1), 
                                                       prefix+AccountMessages.A202.Mess(null, "Bank", deposit, -1)});
                log(LoggingMessages.L608.Mess(args[0], null, change, recAcc));
            }
            else{
                res.setOtherMess(args[0], new String[]{prefix+AccountMessages.A212.Mess(user.getName(), null, change, -1),
                                                       prefix+AccountMessages.A214.Mess(null, recAcc, deposit, -1), 
                                                       prefix+AccountMessages.A235.Mess(null, recAcc, deposit, -1)});
                log(LoggingMessages.L631.Mess(args[0], null, change, recAcc));
            }
            return res;
        }
    },
    
    /**
     * Checks user ranking.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The user's name to check rank of if applicable.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    RANK{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            int rank = 1;
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
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.ACCOUNT);
            if(sorted != null){
                for (String name : sorted.keySet()) {
                    if (self) {
                        if (name.equalsIgnoreCase(user.getName())) {
                            ranked = AccountMessages.A222.Mess(null, null, 0, rank);
                            log(LoggingMessages.L639.Mess(user.getName(), null, 0, null));
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
     * Removes specified amount to specified user's account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The amount to remove and the user's name of the account.
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
            double newbal = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, args[0]) - deduct;
            if(newbal < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], newbal);
            res.setMess(new String[]{prefix+AdminMessages.A303.Mess(args[0], "Account", deduct)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    /**
     * Resets specified user's account is user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The user's name of the account to reset.
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
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            double reset = DCoProperties.getInitialBalance();
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], reset);
            res.setMess(new String[]{prefix+AdminMessages.A301.Mess(args[0], "Account", reset)});
            log(LoggingMessages.L618.Mess(user.getName(), args[0], reset, null));
            return res;
        }
    },
    
    /**
     * Sets specified user's account to specified amount if user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The amount to set and user's name of the account.
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
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            double balance = 0;
            try{
                balance = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (balance < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], balance);
            res.setMess(new String[]{prefix+AdminMessages.A302.Mess(args[0], "Account", balance)});
            log(LoggingMessages.L619.Mess(user.getName(), args[0], balance, null));
            return res;
        }
    },
    
    /**
     * Sets up or turns of a user's Pay Forwarding
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The account to set forwarding to or OFF to turn off Pay Forwarding
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    SETAUTO{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            
            if(!user.canForward()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
            
            if(!argcheck(1, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            
            String account = "Account";
            
            if(!args[0].equalsIgnoreCase("OFF")){
                if(!args[0].equalsIgnoreCase("Bank")){
                    if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                        res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                        return res;
                    }
                    account = args[0];
                }
                else{
                    account = "Bank";
                }
            }
            
            DCoProperties.getDS().setPayForwarding(user.getName(), account);
            
            if(account.equals("Bank")){
                res.setMess(new String[]{prefix+AccountMessages.A215.Mess(null, null, 0, -1)});
                log(LoggingMessages.L635.Mess(user.getName(), null, 0, null));
            }
            else if(account.equalsIgnoreCase("Account")){
                res.setMess(new String[]{prefix+AccountMessages.A217.Mess(null, null, 0, -1)});
                log(LoggingMessages.L636.Mess(user.getName(), null, 0, null));
            }else{
                res.setMess(new String[]{prefix+AccountMessages.A216.Mess(null, account, 0, -1)});
                log(LoggingMessages.L634.Mess(user.getName(), account, 0, null));
            }
            
            return res;
        }
    },
    
    /**
     * Checks the top accounts.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link MoneyCommands}
     * <p>
     * Command Arguments: The amount to check if applicable.
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
            
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.ACCOUNT);
            
            if (sorted.size() < 1) {
                res.setMess(new String[]{ AccountMessages.A223.Mess(null, "Account", 0, 0)});
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
            ranking[0] = AccountMessages.A223.Mess(null, "Account", 0, amount);
            
            for(String name : sorted.keySet()) {
                double balance = sorted.get(name);

                if (rank <= amount) {
                    ranking[rank] = AccountMessages.A224.Mess(null, name, balance, rank);
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
    };
    
    private static final String prefix = "\u00A72[\u00A7adCo \u00A7fMoney\u00A72] ";
    
    private MoneyCommands(){ }
    
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
     */
    public ActionResult execute(User user, String[] args){
        return new ActionResult();
    }
}
