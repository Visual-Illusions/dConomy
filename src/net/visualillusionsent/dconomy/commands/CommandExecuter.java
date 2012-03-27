package net.visualillusionsent.dconomy.commands;

import net.visualillusionsent.dconomy.ActionResult;
import net.visualillusionsent.dconomy.User;
import net.visualillusionsent.dconomy.messages.ErrorMessages;

public class CommandExecuter {
    private static ActionResult defres = new ActionResult();
    
    public static ActionResult execute(User user, String[] args){
        try{
            if(args[0].equals("/money")){
                if(!user.useMoney()){
                    defres.setMess(new String[]{ErrorMessages.E104.Mess(null)});
                    return defres;
                }
                MoneyCommands theCommand = MoneyCommands.valueOf(args[1].toUpperCase());
                String[] newArgs;
                
                if(args.length > 2){
                    newArgs = new String[args.length-2];
                }
                else{
                    newArgs = new String[]{ "" };
                }
                
                for (int i = 2; i < args.length; i++) {
                    newArgs[i-2] = args[i];
                }
                
                return theCommand.execute(user, newArgs);
            }
        }
        catch(IllegalArgumentException IAE){
           defres.setMess(new String[]{ErrorMessages.E113.Mess(null)});
        }
        return defres;
    }
}
