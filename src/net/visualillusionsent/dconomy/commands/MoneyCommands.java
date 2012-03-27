package net.visualillusionsent.dconomy.commands;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.messages.*;
import net.visualillusionsent.dconomy.data.DCoProperties;

/**
 * dConomy money commands
 * 
 * @since   2.0
 * @author  darkdiplomat
 *         <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 */
public enum MoneyCommands {
    
    /**
     * base money command
     */
    BASE{
        
    },
    
    /**
     * money pay command
     */
    PAY{
        /**
         * Pays specified user specified amount.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!argcheck(2, args)){
                res.setMess(new String[]{prefix+ErrorMessages.E120.Mess(null)});
                return res;
            }
            
            if (args[0].equals(user.getName())){
                res.setMess(new String[]{prefix+ErrorMessages.E118.Mess(args[0])});
                return res;
            }
            
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E109.Mess(args[0])});
                return res;
            }
            
            double change = 0;
            
            try{
                change = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{prefix+ErrorMessages.E103.Mess(null)});
                return res;
            }
            
            if (change < 0.01){
                res.setMess(new String[]{prefix+ErrorMessages.E126.Mess(null)});
                return res;
            }
            
            boolean isForwarding = false;
            AccountType sendAT = AccountType.ACCOUNT, recAT = AccountType.ACCOUNT;
            String sendAcc = user.getName(), recAcc = args[0];
            if (DCoProperties.getDS().isPayForwarding(user.getName())){
                isForwarding = true;
                sendAcc = DCoProperties.getDS().getPayForwardingAcc(user.getName());
                if(sendAcc.matches("Bank")){
                    sendAT = AccountType.BANK;
                }
                else{
                    sendAT = AccountType.JOINT;
                }
            }
            
            if (DCoProperties.getDS().isPayForwarding(args[0])){
                isForwarding = true;
                recAcc = DCoProperties.getDS().getPayForwardingAcc(args[0]);
                if(recAcc.equals("Bank")){
                    recAT = AccountType.BANK;
                }
                else{
                    recAT = AccountType.JOINT;
                }
            }
            
            if(isForwarding){
                if (sendAT.equals(AccountType.JOINT)){
                    if(!user.isAdmin() || !DCoProperties.getDS().isJointOwner(sendAcc, user.getName())){
                        if(!DCoProperties.getDS().isJointUser(sendAcc, user.getName())){
                            res.setMess(new String[]{ErrorMessages.E117.Mess(null)});
                            return res;
                        }
                        else if (!DCoProperties.getDS().canWithdraw(user.getName(), sendAcc, change)){
                            res.setMess(new String[]{ErrorMessages.E116.Mess(null)});
                            return res;
                        }
                    }
                }
                if (recAT.equals(AccountType.JOINT)){
                    if(!DCoProperties.getDS().isJointUser(recAcc, args[0])){
                        recAT = AccountType.ACCOUNT;
                    }
                }
            }
            
            double balanceSender = DCoProperties.getDS().getBalance(sendAT, sendAcc);
            double balanceReceiver = DCoProperties.getDS().getBalance(recAT, recAcc);
            double deduct = balanceSender - change;
            double deposit = balanceReceiver + change;
                
            if (deduct < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E101.Mess(null)});
                return res;
            }
              
            DCoProperties.getDS().setBalance(sendAT, sendAcc, deduct);
            DCoProperties.getDS().setBalance(recAT, recAcc, deposit);
            
            if(sendAT.equals(AccountType.ACCOUNT)){
                res.setMess(new String[]{prefix+PlayerMessages.P207.Mess(args[0], null, change),
                                         prefix+PlayerMessages.P206.Mess(null, null, deduct)});
                log(LoggingMessages.L601.Mess(user.getName(), args[0], change, null));
            }
            else if(sendAT.equals(AccountType.BANK)){
                res.setMess(new String[]{prefix+PlayerMessages.P214.Mess(null, null, change),
                                         prefix+PlayerMessages.P207.Mess(args[0], null, change),
                                         prefix+PlayerMessages.P205.Mess(null, null, deduct)});
                log(LoggingMessages.L609.Mess(user.getName(), args[0], change, null));
            }
            else{
                res.setMess(new String[]{prefix+PlayerMessages.P212.Mess(null, sendAcc, change),
                                         prefix+PlayerMessages.P207.Mess(args[0], null, change),
                                         prefix+JointMessages.J302.Mess(null, sendAcc, deduct)});
                log(LoggingMessages.L610.Mess(user.getName(), args[0], change, sendAcc));
            }
            
            if(recAT.equals(AccountType.ACCOUNT)){
                res.setOtherMess(args[0], new String[]{prefix+PlayerMessages.P208.Mess(user.getName(), null, change),
                                                       prefix+PlayerMessages.P206.Mess(null, null, deposit)});
            }
            else if(recAT.equals(AccountType.BANK)){
                res.setOtherMess(args[0], new String[]{prefix+PlayerMessages.P208.Mess(user.getName(), null, change),
                                                       prefix+PlayerMessages.P213.Mess(null, null, deposit), 
                                                       prefix+PlayerMessages.P205.Mess(null, null, deposit)});
                log(LoggingMessages.L608.Mess(args[0], null, change, recAcc));
            }
            else{
                res.setOtherMess(args[0], new String[]{prefix+PlayerMessages.P208.Mess(user.getName(), null, change),
                                                       prefix+PlayerMessages.P211.Mess(null, recAcc, deposit), 
                                                       prefix+JointMessages.J302.Mess(null, recAcc, deposit)});
                log(LoggingMessages.L631.Mess(args[0], null, change, recAcc));
            }
            return res;
        }
    },
    
    RANK{
        
    },
    
    TOP{
        
    },
    
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
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
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
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], balance);
            res.setMess(new String[]{prefix+AdminMessages.A405.Mess(args[0], null, balance)});
            log(LoggingMessages.L619.Mess(user.getName(), args[0], balance, null));
            return res;
        }
    },
    
    RESET{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!user.isAdmin()){
                res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(null)});
                return res;
            }
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{prefix+ErrorMessages.E109.Mess(null)});
                return res;
            }
            double reset = DCoProperties.getInitialBalance();
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, args[0], reset);
            res.setMess(new String[]{prefix+AdminMessages.A404.Mess(args[0], null, reset)});
            log(LoggingMessages.L618.Mess(user.getName(), args[0], reset, null));
            return res;
        }
    },
    
    ADD{
        
    },
    
    REMOVE{
        
    },
    
    AUTO{
        
    },
    
    SETAUTO{
        
    },
    
    HELP{
        
    };
    
    private static final String prefix = "\u00A72[\u00A7fdCo-Money\u00A72] ";
    
    private MoneyCommands(){ }
    
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
