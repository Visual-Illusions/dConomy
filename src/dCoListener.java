import net.visualillusionsent.dconomy.*;
import net.visualillusionsent.dconomy.commands.CommandExecuter;
import net.visualillusionsent.dconomy.data.DCoProperties;
/**
 * dCoListener.java - dConomy's Plugin Listener class
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class dCoListener extends PluginListener{
    
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
            if(player.canUseCommand("/money")){
                if(!DCoProperties.getDS().AccountExists(AccountType.ACCOUNT, player.getName())){
                    DCoProperties.getDS().setInitialBalance(AccountType.ACCOUNT, player.getName());
                }
            }
            if(player.canUseCommand("/bank")){
                if(!DCoProperties.getDS().AccountExists(AccountType.BANK, player.getName())){
                    DCoProperties.getDS().setInitialBalance(AccountType.BANK, player.getName());
                }
            }
        }
    }
    
    /**
     * Listens for dConomy commands and forwards them to {@link CommandExecuter#execute(User, String[])}
     * 
     * @param player Player using the command
     * @param args String array of the command and arguments
     * 
     * @return true if dConomy command, false otherwise
     * 
     * @see CommandExecuter#execute(User, String[])
     * @see ActionResult
     * @see User
     */
    public boolean onCommand(Player player, String[] args){
        if(args[0].matches("/money|/joint|/bank")){
            User user = new User(player.getName(), can(player, "/money"), can(player, "/bank"), can(player, "/joint"),
                    can(player, "/dcrank"), can(player, "/dccreate"), can(player, "/dcauto"), can(player, "/dcadmin"));
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

/*******************************************************************************\
* dConomy                                                                       *
* Copyright (C) 2011-2012 Visual Illusions Entertainment                        *
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of dConomy.                                                 *                       
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
* GNU General Public License for more details.                                  *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.   *
\*******************************************************************************/