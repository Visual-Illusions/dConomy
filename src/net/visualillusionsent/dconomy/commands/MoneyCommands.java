package net.visualillusionsent.dconomy.commands;

import java.util.Map;

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
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            if(!args[0].equals("") && !DCoProperties.getAOC()){
                if (!DCoProperties.getDS().AccountExists(AccountType.BANK, args[0])){
                    res.setMess(new String[]{prefix+ErrorMessages.E104.Mess(args[0])});
                    return res; 
                }
                res.setMess(new String[]{prefix+AccountMessages.A203.Mess(args[0], "Account", DCoProperties.getDS().getBalance(AccountType.ACCOUNT, args[0]), -1)});
                return res;
            }
            else{
                res.setMess(new String[]{prefix+AccountMessages.A201.Mess(null, "Account", DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()), -1)});
                return res;
            }
        }
    },
    
    /**
     * money pay command
     */
    PAY{
        /**
         * Pays specified user specified amount.
         * 
         * @param user  The user calling the command.
         * @param args  The command arguments of user to pay and amount to pay.
         * 
         * @return res  ActionResult containing messages to be sent.
         * @since   2.0
         */
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
                else if(DCoProperties.getDS().isJointOwner(sendAcc, user.getName())){
                    sendAT = AccountType.JOINT;
                }
            }
            
            if (DCoProperties.getDS().isPayForwarding(args[0])){
                recAcc = DCoProperties.getDS().getPayForwardingAcc(args[0]);
                if(recAcc.equals("Bank")){
                    recAT = AccountType.BANK;
                }
                else if(DCoProperties.getDS().isJointUser(recAcc, args[0])){
                    recAT = AccountType.JOINT;
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
            res.setMess(new String[]{prefix+AdminMessages.A302.Mess(args[0], null, balance)});
            log(LoggingMessages.L619.Mess(user.getName(), args[0], balance, null));
            return res;
        }
    },
    
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
            res.setMess(new String[]{prefix+AdminMessages.A301.Mess(args[0], null, reset)});
            log(LoggingMessages.L618.Mess(user.getName(), args[0], reset, null));
            return res;
        }
    },
    
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
            double newbal = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) + deposit;
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newbal);
            res.setMess(new String[]{prefix+AdminMessages.A304.Mess(args[0], null, newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        } 
    },
    
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
            double newbal = DCoProperties.getDS().getBalance(AccountType.ACCOUNT, user.getName()) - deduct;
            if(newbal < 0){
                res.setMess(new String[]{prefix+ErrorMessages.E102.Mess(null)});
                return res;
            }
            DCoProperties.getDS().setBalance(AccountType.ACCOUNT, user.getName(), newbal);
            res.setMess(new String[]{prefix+AdminMessages.A303.Mess(args[0], null, newbal)});
            log(LoggingMessages.L623.Mess(user.getName(), args[0], newbal, null));
            return res;
        }
    },
    
    AUTO{
        
    },
    
    SETAUTO{
        
    },
    
    HELP{
        public ActionResult execute(User user, String[] args){
            ActionResult res = new ActionResult();
            res.setMess(new String[]{HelpMessages.H501.Mess(),
                                     HelpMessages.H502.Mess(),
                                     HelpMessages.H540.Mess(),
                                     HelpMessages.H503.Mess(),
                                     HelpMessages.H504.Mess(),
                                     (user.canRank() ? HelpMessages.H505.Mess() : null),
                                     (user.canRank() ? HelpMessages.H506.Mess() : null),
                                     (user.canForward() ? HelpMessages.H511.Mess() : null),
                                     (user.canForward() ? HelpMessages.H512.Mess() : null),
                                     (user.useBank() ? HelpMessages.H513.Mess() : null),
                                     (user.useJoint() ? HelpMessages.H514.Mess() : null),
                                     (user.isAdmin() ? HelpMessages.H541.Mess() : null)});
            return res;
        }
    };
    
    private static final String prefix = "\u00A72[\u00A7adCo \u00A7fMoney\u00A72] ";
    
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
