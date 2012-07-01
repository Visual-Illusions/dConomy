import java.util.logging.Level;
import java.util.logging.Logger;

import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.commands.CommandExecuter;
import net.visualillusionsent.dconomy.data.DCoProperties;
import net.visualillusionsent.dconomy.data.SaveCaller;
import net.visualillusionsent.dconomy.messages.ErrorMessages;

/**
 * dConomy's Plugin Listener class
 * <p>
 * This file is part of {@link dConomy}
 * 
 * @since   2.0
 * @author  darkdiplomat
 */
public class dCoListener extends PluginListener{
    Logger logger = Logger.getLogger("Minecraft");
    
    /**
     * Listens for player logins and creates accounts if necessary
     * 
     * @param   player Player logging in
     * @since   2.0
     */
    public void onLogin(Player player){
        if(DCoProperties.getCAA()){
            if(!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, player.getName())){
                DCoProperties.getDS().setInitialBalance(AccountType.ACCOUNT, player.getName());
            }
            if(!DCoProperties.getDS().AccountExists(AccountType.BANK, player.getName())){
                DCoProperties.getDS().setInitialBalance(AccountType.BANK, player.getName());
            }
        }
        else{
            if(can(player, "/money")){
                if(!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, player.getName())){
                    DCoProperties.getDS().setInitialBalance(AccountType.ACCOUNT, player.getName());
                }
            }
            if(can(player, "/bank")){
                if(!DCoProperties.getDS().AccountExists(AccountType.BANK, player.getName())){
                    DCoProperties.getDS().setInitialBalance(AccountType.BANK, player.getName());
                }
            }
        }
    }
    
    /**
     * Listens for dConomy commands and forwards them to {@link CommandExecuter#execute(User, String[])}
     * 
     * @param   player Player using the command
     * @param   args String array of the command and arguments
     * @return  true if dConomy command, false otherwise
     * @since   2.0
     * @see     CommandExecuter#execute(User, String[])
     * @see     ActionResult
     * @see     User
     */
    public boolean onCommand(Player player, String[] args){
        if(args[0].matches("/money|/joint|/bank|/dConomy")){
            try{
                User user = new User(player.getName(), can(player, "/money"), can(player, "/bank"), can(player, "/joint"),
                                 can(player, "/dcrank"), can(player, "/dccreate"), can(player, "/dcauto"), can(player, "/dcadmin"), new MiscImpl());
                args[0] = args[0].substring(1);
                ActionResult res = CommandExecuter.execute(user, args);
                for(String message : res.getMess()){
                    if(message != null){
                        player.sendMessage(message);
                    }
                }
                if(res.getOtherReceiver() != null){
                    Player other = etc.getServer().getPlayer(res.getOtherReceiver());
                    if(other != null && other.isConnected()){
                        for(String message : res.getOtherMess()){
                            if(message != null){
                                other.sendMessage(message);
                            }
                        }
                    }
                }
            }
            catch(Exception E){
                player.sendMessage("\u00A72[\u00A7fdCo\u00A72]\u00A7c An exception has occured in dConomy...");
                logger.log(Level.SEVERE, "[dConomy] An uncaught exception has occured!", E);
            }
            return true;
        }
        else if(args[0].equalsIgnoreCase("/#stop")){
            if(player.isOp()){
                new SaveCaller().run();
            }
        }
        return false;
    }
    
    /**
     * Listens for dConomy console commands and forwards them to {@link CommandExecuter#execute(User, String[])}
     * 
     * @param   args String array of the command and arguments
     * @return  true if dConomy command, false otherwise
     * @since   2.0
     * @see     CommandExecuter#execute(User, String[])
     * @see     ActionResult
     * @see     User
     */
    public boolean onConsoleCommand(String[] args){
        if(args[0].matches("money|joint|bank")){
            try{
                if(args.length > 1){
                    User user = new User("SERVER", true, true, true, true, true, true, true, new MiscImpl());
                    ActionResult res = CommandExecuter.execute(user, args);
                    for(String message : res.getMess()){
                        if(message != null){
                            System.out.println(removeFormating(message));
                        }
                    }
                }
                else{
                    System.out.println(removeFormating(ErrorMessages.E103.Mess(null, null)));
                }
            }
            catch(Exception E){
                logger.log(Level.SEVERE, "[dConomy] An uncaught exception has occured!", E);
            }
            return true;
        }
        else if(args[0].equalsIgnoreCase("stop")){
            new SaveCaller().run();
        }
        return false;
    }
    
    private boolean can(Player player, String cmd){
        return player.canUseCommand(cmd) || player.canUseCommand("/dcadmin");
    }
    
    private String removeFormating(String message){
        String newmess = message;
        newmess = newmess.replace("\u00A70", "");
        newmess = newmess.replace("\u00A71", "");
        newmess = newmess.replace("\u00A72", "");
        newmess = newmess.replace("\u00A73", "");
        newmess = newmess.replace("\u00A74", "");
        newmess = newmess.replace("\u00A75", "");
        newmess = newmess.replace("\u00A76", "");
        newmess = newmess.replace("\u00A77", "");
        newmess = newmess.replace("\u00A78", "");
        newmess = newmess.replace("\u00A79", "");
        newmess = newmess.replace("\u00A7a", "");
        newmess = newmess.replace("\u00A7b", "");
        newmess = newmess.replace("\u00A7c", "");
        newmess = newmess.replace("\u00A7d", "");
        newmess = newmess.replace("\u00A7e", "");
        newmess = newmess.replace("\u00A7f", "");
        newmess = newmess.replace("\u00A7l", "");
        newmess = newmess.replace("\u00A7m", "");
        newmess = newmess.replace("\u00A7n", "");
        newmess = newmess.replace("\u00A7o", "");
        newmess = newmess.replace("\u00A7r", "");
        return newmess;
    }
}
