import net.visualillusionsent.dconomy.ActionResult;
import net.visualillusionsent.dconomy.User;
import net.visualillusionsent.dconomy.commands.CommandExecuter;

public class dCoListener extends PluginListener{
    
    public boolean onCommand(Player player, String[] args){
        User user = new User(player.getName(), can(player, "/money"), can(player, "/bank"), can(player, "/joint"),
                      can(player, "/dcrank"), can(player, "/dccreate"), can(player, "/dcauto"), can(player, "/dcadmin"));
        if(args[0].matches("/money|/joint|/bank")){
            ActionResult res = CommandExecuter.execute(user, args);
            for(String message : res.getMess()){
                player.sendMessage(message);
            }
            if(res.getOtherReceiver() != null){
                Player other = etc.getServer().getPlayer(res.getOtherReceiver());
                if(other != null && other.isConnected()){
                    for(String message : res.getOtherMess()){
                        other.sendMessage(message);
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean can(Player player, String cmd){
        return player.canUseCommand(cmd);
    }
}
