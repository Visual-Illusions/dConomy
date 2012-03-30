package net.visualillusionsent.dconomy.commands;

import java.util.Map;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.messages.AccountMessages;
import net.visualillusionsent.dconomy.messages.AdminMessages;
import net.visualillusionsent.dconomy.messages.ErrorMessages;
import net.visualillusionsent.dconomy.messages.HelpMessages;
import net.visualillusionsent.dconomy.messages.LoggingMessages;

/**
 * dConomy joint commands
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public enum JointCommands {
    
    /**
     * Adds specified amount to specified Joint Account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The amount to add and name of the account.
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
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
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
            
            double newbal = DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]) + deposit;
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], newbal);
            res.setMess(new String[]{prefix+AdminMessages.A307.Mess(null, args[0], newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    /**
     * Adds specified user to specified Joint Account's owner list
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the user to add and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    ADDOWNER{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            if(DCoProperties.getDS().isJointOwner(args[0], args[1])){
                res.setMess(new String[]{prefix+ErrorMessages.E112.Mess(args[0])});
                return res;
            }
            
            DCoProperties.getDS().addJointOwner(args[0], args[1]);
            res.setMess(new String[]{prefix+AccountMessages.A227.Mess(args[1], args[0], -1, -1)});
            log(LoggingMessages.L613.Mess(user.getName(), args[1], -1, args[0])); 
            return res;
        }
    },
    
    /**
     * Adds specified user to specified Joint Account's user list
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the user to add and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    ADDUSER{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            if(DCoProperties.getDS().isJointUser(args[0], args[1])){
                res.setMess(new String[]{prefix+ErrorMessages.E111.Mess(args[0])});
                return res;
            }
            
            DCoProperties.getDS().addJointUser(args[0], args[1]);
            res.setMess(new String[]{prefix+AccountMessages.A229.Mess(args[1], args[0], -1, -1)});
            log(LoggingMessages.L615.Mess(user.getName(), args[1], -1, args[0]));
            return res;
        }
    },
    
    /**
     * Checks account balance of the Joint Account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * The command arguments: Name of the account
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    BASE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointUser(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            
            res.setMess(new String[]{prefix+AccountMessages.A234.Mess(null, args[0], DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]), -1)});
            return res;
        }
    },
    
    /**
     * Creates a Joint Account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * The command arguments: Name of the account
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    CREATE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.canCreate()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res; 
            }
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E106.Mess(args[0])});
                return res;
            }
            if(!args[0].matches("[_a-zA-Z0-9\\-\\.]+")){
                res.setMess(new String[]{prefix+ErrorMessages.E107.Mess(args[0])});
                return res;
            }
            DCoProperties.getDS().createJointAccount(args[0], user.getName());
            res.setMess(new String[]{prefix+AccountMessages.A225.Mess(null, args[0], -1, -1)});
            log(LoggingMessages.L611.Mess(user.getName(), null, -1, args[0]));
            return res;
        }
    },
    
    /**
     * Deletes a Joint Account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * The command arguments: Name of the account
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    DELETE{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.canCreate()){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res; 
            }
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            DCoProperties.getDS().deleteJointAccount(args[0]);
            res.setMess(new String[]{prefix+AccountMessages.A226.Mess(null, args[0], -1, -1)});
            log(LoggingMessages.L612.Mess(user.getName(), null, -1, args[0]));
            return res;
        }
    },
    
    /**
     * Deposits specified amount into the Joint Account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * Command Arguments: The amount to be deposited.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    DEPOSIT{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointUser(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            double deposit = 0;
            try{
                deposit = Double.parseDouble(args[1]);
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
            double newBal = DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]) + deposit;
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], newBal);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+AccountMessages.A207.Mess(null, null, deposit, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Account", newAcc, -1), 
                                     prefix+AccountMessages.A235.Mess(null, args[0], newBal, -1)});
            log(LoggingMessages.L605.Mess(user.getName(), null, deposit, args[0]));
            return res;
        }
    },
    
    /**
     * Displays help information.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: none needed
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    HELP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            res.setMess(new String[]{HelpMessages.H403.Mess(),
                                     HelpMessages.H404.Mess(),
                                     HelpMessages.H405.Mess(),
                                     HelpMessages.H411.Mess(),
                                     HelpMessages.H419.Mess(),
                                     HelpMessages.H420.Mess(),
                                     HelpMessages.H421.Mess(),
                                     HelpMessages.H424.Mess(),
                                     HelpMessages.H425.Mess(),
                                     HelpMessages.H426.Mess(),
                                     HelpMessages.H427.Mess(),
                                     HelpMessages.H428.Mess(),
                                     HelpMessages.H429.Mess(),
                                     HelpMessages.H430.Mess(),
                                     (user.canCreate() ? HelpMessages.H422.Mess() : null),
                                     (user.canCreate() ? HelpMessages.H423.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H431.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H432.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H433.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H434.Mess() : null),
                                     (user.useMoney() ? HelpMessages.H406.Mess() : null),
                                     (user.useBank() ? HelpMessages.H407.Mess() : null)
                                     });
            return res;
        }
    },
    
    /**
     * Pays specified Joint Account specified amount.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the account to pay and amount to pay.
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
        
            if (!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
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
        
            AccountType sendAT = AccountType.ACCOUNT;
            String sendAcc = user.getName();
            if (DCoProperties.getDS().isPayForwarding(user.getName()) && user.canForward()){
                sendAcc = DCoProperties.getDS().getPayForwardingAcc(user.getName());
                if(sendAcc.matches("Bank")){
                    sendAT = AccountType.BANK;
                }
                else if(DCoProperties.getDS().AccountExists(AccountType.JOINT, sendAcc) && DCoProperties.getDS().isJointUser(sendAcc, user.getName())){
                    if(!user.isAdmin() || !DCoProperties.getDS().isJointOwner(sendAcc, user.getName())){
                        if(DCoProperties.getDS().canWithdraw(user.getName(), sendAcc, change)){
                            DCoProperties.getDS().addJUWD(sendAcc, user.getName(), change);
                            sendAT = AccountType.JOINT;
                        }
                        else{
                            res.setMess(new String[]{prefix+(change > DCoProperties.getDS().getJointUserWithdrawMax(args[0]) ? ErrorMessages.E110.Mess(args[0]) : ErrorMessages.E109.Mess(args[0]))});
                            return res;
                        }
                    }
                }
            }
            
            double balanceSender = DCoProperties.getDS().getBalance(sendAT, sendAcc);
            double balanceReceiver = DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]);
            double deduct = balanceSender - change;
            double deposit = balanceReceiver + change;
            
            if (deduct < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E115.Mess(null)});
                return res;
            }
          
            DCoProperties.getDS().setBalance(sendAT, sendAcc, deduct);
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], deposit);

            if(sendAT.equals(AccountType.ACCOUNT)){
                res.setMess(new String[]{prefix+AccountMessages.A209.Mess(null, args[0], change, -1),
                                         prefix+AccountMessages.A202.Mess(null, "Account", deduct, -1)});
                log(LoggingMessages.L601.Mess(user.getName(), args[0], change, null));
            }
            else if(sendAT.equals(AccountType.BANK)){
                res.setMess(new String[]{prefix+AccountMessages.A209.Mess(null, args[0], change, -1),
                                         prefix+AccountMessages.A210.Mess(null, "Bank", change, -1),
                                         prefix+AccountMessages.A202.Mess(null, "Bank", deduct, -1)});
                log(LoggingMessages.L609.Mess(user.getName(), args[0], change, null));
            }
            else{
                res.setMess(new String[]{prefix+AccountMessages.A209.Mess(null, args[0], change, -1),
                                         prefix+AccountMessages.A211.Mess(null, sendAcc, change, -1),
                                         prefix+AccountMessages.A235.Mess(null, sendAcc, deduct, -1)});
                log(LoggingMessages.L610.Mess(user.getName(), args[0], change, sendAcc));
            }
        
            return res;
        }
    },
    
    /**
     * Checks account ranking.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The account to check rank of.
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
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                return res;
            }
            
            String ranked = null;
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.JOINT);
            if(sorted != null){
                for (String name : sorted.keySet()) {
                    if (name.equalsIgnoreCase(args[0])) {
                        ranked = AccountMessages.A222.Mess(args[0], null, 0, rank);
                        log(LoggingMessages.L639.Mess(user.getName(), args[0], 0, null));
                        break;
                    }
                    rank++;
                }
            }
            
            res.setMess(new String[]{ ranked != null ? prefix+rank : prefix+"Unable to retrieve rank..." });
            return res;
        }
    },
    
    /**
     * Removes specified amount from specified Joint Account
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The amount to remove and name of the account.
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
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
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
            double newbal = DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]) - deduct;
            if(newbal < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], newbal);
            res.setMess(new String[]{prefix+AdminMessages.A308.Mess(null, args[0], newbal)});
            log(LoggingMessages.L629.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    /**
     * Removes specified user from specified Joint Account's owner list
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the user to add and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    REMOVEOWNER{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            if(!DCoProperties.getDS().isJointOwner(args[0], args[1])){
                res.setMess(new String[]{prefix+ErrorMessages.E114.Mess(args[0])});
                return res;
            }
            
            DCoProperties.getDS().removeJointOwner(args[0], args[1]);
            res.setMess(new String[]{prefix+AccountMessages.A228.Mess(args[1], args[0], -1, -1)});
            log(LoggingMessages.L616.Mess(user.getName(), args[1], -1, args[0])); 
            return res;
        }
    },
    
    /**
     * Removes specified user from specified Joint Account's user list
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the user to add and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    REMOVEUSER{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            if(!DCoProperties.getDS().isAbsoluteJointUser(args[0], args[1])){
                res.setMess(new String[]{prefix+ErrorMessages.E114.Mess(args[0])});
                return res;
            }
            
            DCoProperties.getDS().removeJointOwner(args[0], args[1]);
            res.setMess(new String[]{prefix+AccountMessages.A230.Mess(args[1], args[0], -1, -1)});
            log(LoggingMessages.L617.Mess(user.getName(), args[1], -1, args[0]));
            return res;
        }
    },
    
    /**
     * Resets specified Joint Account is user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the account to reset.
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
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], 0);
            res.setMess(new String[]{prefix+AdminMessages.A305.Mess(null, args[0], 0)});
            log(LoggingMessages.L626.Mess(user.getName(), null, 0, args[0]));
            return res;
        } 
    },
    
    /**
     * Sets specified Joint Account to specified amount if user calling command is admin.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The amount to set and name of the account.
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
            if (!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
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
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], balance);
            res.setMess(new String[]{prefix+AdminMessages.A306.Mess(null, args[0], balance)});
            log(LoggingMessages.L627.Mess(user.getName(), null, balance, args[0]));
            return res;
        }
    },
    
    /**
     * Sets specified Joint Account's withdraw delay.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The amount to set and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    SETDELAY{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(args[0].equals("")){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            
            int delay = 0;
            try{
                delay = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (delay < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            
            DCoProperties.getDS().setJointDelay(args[0], delay);
            res.setMess(new String[]{prefix+AccountMessages.A233.Mess(null, args[0], -1, -1)});
            log(LoggingMessages.L616.Mess(user.getName(), null, delay, args[0])); 
            return res;
        }
    },
    
    /**
     * Sets specified Joint Account's max user withdraw.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The amount to set and name of the account.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    SETUSERMAX{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointOwner(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            double newamount = 0;
            try{
                newamount = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (newamount < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setJointMaxUserWithdraw(args[0], newamount);
            res.setMess(new String[]{prefix+AccountMessages.A232.Mess(user.getName(), args[0], newamount, -1)});
            log(LoggingMessages.L637.Mess(user.getName(), null, newamount, args[0])); 
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
            
            Map<String, Double> sorted = DCoProperties.getDS().getRankMap(AccountType.JOINT);
            
            if (sorted.size() < 1) {
                res.setMess(new String[]{ AccountMessages.A223.Mess(null, "Joint Account", 0, 0)});
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
            ranking[0] = AccountMessages.A223.Mess(null, "Joint Account", 0, amount);
            
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
    },
    
    /**
     * Withdraw specified amount from Joint Account.
     * <p>
     * Will override {@link #execute(User, String[])} in {@link JointCommands}
     * <p>
     * Command Arguments: The name of the account and the amount to be withdrawn.
     * 
     * @since   2.0
     * @see #execute(User, String[])
     */
    WITHDRAW{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.JOINT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E105.Mess(args[0])});
                return res;
            }
            if(!user.isAdmin() && !DCoProperties.getDS().isJointUser(args[0], user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E108.Mess(args[0])});
                return res;
            }
            double withdraw = 0;
            try{
                withdraw = Double.parseDouble(args[1]);
            }
            catch(NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if (withdraw < 0.01){ 
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            if(DCoProperties.getDS().canWithdraw(user.getName(), args[0], withdraw)){
                DCoProperties.getDS().addJUWD(args[0], user.getName(), withdraw);
            }
            else{
                res.setMess(new String[]{prefix+(withdraw > DCoProperties.getDS().getJointUserWithdrawMax(args[0]) ? ErrorMessages.E110.Mess(args[0]) : ErrorMessages.E109.Mess(args[0]))});
                return res;
            }
            double newBank = DCoProperties.getDS().getBalance(AccountType.JOINT, args[0]) - withdraw;
            if (newBank < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E116.Mess(null)});
                return res;
            }
            double newAcc = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) + withdraw;
            DCoProperties.getDS().setBalance(AccountType.JOINT, args[0], newBank);
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newAcc);
            res.setMess(new String[]{prefix+AccountMessages.A206.Mess(null, null, withdraw, -1), 
                                     prefix+AccountMessages.A202.Mess(null, "Account", newAcc, -1), 
                                     prefix+AccountMessages.A235.Mess(null, args[0], newBank, -1)});
            log(LoggingMessages.L603.Mess(user.getName(), null, withdraw, null));
            return res;
        }
    };
    
    private static final String prefix = "\u00A72[\u00A7adCo \u00A7fJoint\u00A72] ";
    
    private JointCommands(){ }
    
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
