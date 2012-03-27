package net.visualillusionsent.dconomy.commands;

import net.visualillusionsent.dconomy.AccountType;
import net.visualillusionsent.dconomy.ActionResult;
import net.visualillusionsent.dconomy.User;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.messages.*;

public enum MoneyCommands {
    
    /**
     * Pays specified player specified amount
     */
    PAY{
        @Override
        public ActionResult execute(User user, String[] args){
            if(!argcheck(2, args)){
                res.setMess(new String[]{ErrorMessages.E106.Mess(null)});
                return res;
            }
            
            if (args[0].equals(user.getName())){
                res.setMess(new String[]{ErrorMessages.E118.Mess(args[0])});
                return res;
            }
            
            if (!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, args[0])){
                res.setMess(new String[]{ErrorMessages.E109.Mess(args[0])});
                return res;
            }
            
            double change = 0;
            
            try{
                change = Double.parseDouble(args[1]);
            }catch (NumberFormatException nfe){
                res.setMess(new String[]{ErrorMessages.E103.Mess(null)});
                return res;
            }
            
            if (change < 0.01){
                res.setMess(new String[]{ErrorMessages.E126.Mess(null)});
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
                res.setMess(new String[]{ErrorMessages.E101.Mess(null)});
                return res;
            }
              
            DCoProperties.getDS().setBalance(sendAT, sendAcc, deduct);
            DCoProperties.getDS().setBalance(recAT, recAcc, deposit);
            
            if(sendAT.equals(AccountType.ACCOUNT)){
                res.setMess(new String[]{PlayerMessages.P207.Mess(args[0], null, change), PlayerMessages.P206.Mess(null, null, deduct)});
            }
            else if(sendAT.equals(AccountType.BANK)){
                res.setMess(new String[]{PlayerMessages.P214.Mess(null, null, change), PlayerMessages.P207.Mess(args[0], null, change), PlayerMessages.P205.Mess(null, null, deduct)});
                //dCD.Logging(609, sender, receiver, String.valueOf(amount), ""); TODO
            }
            else{
                res.setMess(new String[]{PlayerMessages.P212.Mess(null, sendAcc, change), PlayerMessages.P207.Mess(args[0], null, change), JointMessages.J302.Mess(null, sendAcc, deduct)});
                //dCD.Logging(610, sender, receiver, String.valueOf(amount), payfrom); TODO
            }
            
            if(recAT.equals(AccountType.ACCOUNT)){
                res.setOtherMess(args[0], new String[]{PlayerMessages.P208.Mess(user.getName(), null, change), PlayerMessages.P206.Mess(null, null, deposit)});
                //dCD.Logging(601, sender, receiver, amount, ""); TODO
            }
            else if(recAT.equals(AccountType.BANK)){
                res.setOtherMess(args[0], new String[]{PlayerMessages.P208.Mess(user.getName(), null, change), PlayerMessages.P213.Mess(null, null, deposit), PlayerMessages.P205.Mess(null, null, deposit)});
                //dCD.Logging(608, sender, receiver, String.valueOf(amount), payto); TODO
            }
            else{
                res.setOtherMess(args[0], new String[]{PlayerMessages.P208.Mess(user.getName(), null, change), PlayerMessages.P211.Mess(null, recAcc, deposit), PlayerMessages.P205.Mess(null, recAcc, deposit)});
                //dCD.Logging(631, sender, receiver, String.valueOf(amount), payto); TODO
            }
            
            return res;
        }
    },
    
    RANK{
        
    },
    
    TOP{
        
    },
    
    SET{
        
    },
    
    RESET{
        
    },
    
    ADD{
        
    },
    
    REMOVE{
        
    },
    
    AUTO{
        
    },
    
    SETAUTO{
        
    };
    
    private static ActionResult res = new ActionResult();
    
    private MoneyCommands(){ }
    
    private static boolean argcheck(int length, String[] args){
        return args.length >= length;
    }
    
    public ActionResult execute(User user, String[] args){
        return new ActionResult();
    }
}
