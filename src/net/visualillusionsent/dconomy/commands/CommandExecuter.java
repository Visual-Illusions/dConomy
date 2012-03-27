package net.visualillusionsent.dconomy.commands;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import net.visualillusionsent.dconomy.ActionResult;
import net.visualillusionsent.dconomy.User;
import net.visualillusionsent.dconomy.messages.ErrorMessages;

/**
 * CommandExecuter.java dConomy Command handling class
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class CommandExecuter {
    private static ActionResult defres = new ActionResult();
    public static final String name = "dConomy";
    public static final float version = 2.0F;
    public static float currver = version;
    
    /**
     * dConomy Command execution handler
     * 
     * @param user User calling the command
     * @param args Command arguments
     * @return defres ActionResult containing any messages to be sent to the player
     * 
     * @since dConomy v2.0
     * 
     * @see ActionResult
     * @see User
     * @see MoneyCommands
     */
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
           defres.setMess(new String[]{"\u00A72[\u00A7fdCo\u00A72]\u00A7f "+ErrorMessages.E113.Mess(null)});
        }
        return defres;
    }
    
    /**
     * Checks is dConomy is the latest version
     * 
     * @return true if it is
     * @since dConomy v2.0
     */
    public static final boolean isLatest(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name).openStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                currver = Float.valueOf(inputLine);
            }
            in.close();
            return version >= currver;
        } 
        catch (Exception E) { }
        return true;
    }
}
