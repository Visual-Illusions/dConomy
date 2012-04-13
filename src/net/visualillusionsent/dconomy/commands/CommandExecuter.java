package net.visualillusionsent.dconomy.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.visualillusionsent.dconomy.ActionResult;
import net.visualillusionsent.dconomy.User;
import net.visualillusionsent.dconomy.messages.ErrorMessages;

/**
 * dConomy command handling class
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class CommandExecuter {
    private static ActionResult defres = new ActionResult();
    public static final String name = "dConomy";
    public static final String version = "2.0_2";
    public static String currver = version;
    
    /**
     * dConomy Command execution handler.
     * 
     * @param   user    User calling the command.
     * @param   args    The command arguments.
     * @return  defres  ActionResult containing any messages to be sent to the player.
     * @since   2.0
     * @see     ActionResult
     * @see     User
     * @see     MoneyCommands
     * @see     BankCommands
     * @see     JointCommands
     */
    public static ActionResult execute(User user, String[] args){
        try{
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
            
            if(args[0].equalsIgnoreCase("money")){
                if(!user.useMoney()){
                    defres.setMess(new String[]{ErrorMessages.E101.Mess(null, null)});
                    return defres;
                }
                
                if(args.length == 1){
                    return MoneyCommands.BASE.execute(user, newArgs);
                }
                
                boolean isBase = true;
                for(MoneyCommands cmd : MoneyCommands.values()){
                    if(args[1].toUpperCase().matches(cmd.name())){
                        isBase = false;
                    }
                }
                
                if(isBase){
                    return MoneyCommands.BASE.execute(user, new String[]{ args[1] });
                }
                
                MoneyCommands theCommand = MoneyCommands.valueOf(args[1].toUpperCase());
                
                
                return theCommand.execute(user, newArgs);
            }
            else if(args[0].equalsIgnoreCase("bank")){
                if(!user.useBank()){
                    defres.setMess(new String[]{ErrorMessages.E101.Mess(null, null)});
                    return defres;
                }
                
                if(args.length == 1){
                    return BankCommands.BASE.execute(user, newArgs);
                }
                
                boolean isBase = true;
                for(BankCommands cmd : BankCommands.values()){
                    if(args[1].toUpperCase().matches(cmd.name())){
                        isBase = false;
                    }
                }
                
                if(isBase){
                    return BankCommands.BASE.execute(user, new String[]{ args[1] });
                }
                
                BankCommands theCommand = BankCommands.valueOf(args[1].toUpperCase());
                
                return theCommand.execute(user, newArgs);
            }
            else if(args[0].equalsIgnoreCase("joint")){
                if(!user.useJoint()){
                    defres.setMess(new String[]{ErrorMessages.E101.Mess(null, null)});
                    return defres;
                }
                
                if(args.length < 2){
                    defres.setMess(new String[]{ErrorMessages.E103.Mess(null, null)});
                    return defres;
                }
                
                boolean isBase = true;
                
                for(JointCommands cmd : JointCommands.values()){
                    if(args[1].toUpperCase().matches(cmd.name())){
                        isBase = false;
                    }
                }
                
                if(isBase){
                    return JointCommands.BASE.execute(user, new String[]{ args[1] });
                }
                
                JointCommands theCommand = JointCommands.valueOf(args[1].toUpperCase());
                
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
           defres.setMess(new String[]{"\u00A72[\u00A7fdCo\u00A72]\u00A7f Invaild dConomy Command!"});
        }
        return defres;
    }
    
    /**
     * Checks is dConomy is the latest version
     * 
     * @return true if it is
     * @since   2.0
     */
    public static final boolean isLatest(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name).openStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                currver = inputLine;
            }
            in.close();
            return Float.valueOf(version.replace("_", "")) >= Float.valueOf(currver.replace("_", ""));
        } 
        catch (Exception E) { }
        return true;
    }
}
