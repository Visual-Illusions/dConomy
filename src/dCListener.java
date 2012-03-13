
public class dCListener extends PluginListener{
    dCData dCD;
    dConomy dCo;
    dCActionHandler dCAH;
    String S = "SERVER";
    String A = "Account";
    String B = "Bank";
    
    public dCListener(dConomy dCo, dCData dCD){
        this.dCo = dCo;
        this.dCD = dCD;
        this.dCAH = new dCActionHandler(dCo);
    }
    
    public boolean onCommand(Player player, String[] cmd){
        if (cmd[0].equalsIgnoreCase("/money")){
            if (player.canUseCommand("/money")){
                if (cmd.length == 1){
                    return dCAH.DisplayBalance(player.getName(), 1, "", (player.canUseCommand("/dcadmin")));
                }else if ((cmd[1].equals("pay")) || (cmd[1].equals("-p"))){
                    if (cmd.length < 3){
                        return dCAH.Error(120, player.getName(), "");
                    }else if (cmd.length < 4){
                        return dCAH.Error(106, player.getName(), "");
                    }else{
                        return dCAH.PayPlayer(player.getName(), cmd[2], cmd[3], player.canUseCommand("/dcadmin"));
                    }
                }else if ((cmd[1].equals("top")) || (cmd[1].equals("-t"))){
                    if((player.canUseCommand("/dcrank")) || (player.canUseCommand("/dcadmin"))){
                        if (cmd.length < 3){
                            return dCAH.RankTop(player.getName(), "5");
                        }else{
                            return dCAH.RankTop(player.getName(), cmd[2]);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("rank")) || (cmd[1].equals("-r"))){
                    if((player.canUseCommand("/dcrank")) || (player.canUseCommand("/dcadmin"))){
                        if (cmd.length < 3){
                            return dCAH.RankPlayer(player.getName(), "", true);
                        }else{
                            return dCAH.RankPlayer(player.getName(), cmd[2], false);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("help")) || (cmd[1].equals("?"))){
                    if (cmd.length > 2){
                        if (cmd[2].equals("admin")){
                            if (player.canUseCommand("/dcadmin")){
                                return dCAH.MoneyAdminHelp(player.getName());
                            }
                        }
                    }
                    return dCAH.MoneyHelp(player.getName(), player.canUseCommand("/dcrank"), player.canUseCommand("/dcadmin"), player.canUseCommand("/dcauto"), player.canUseCommand("/bank"), player.canUseCommand("/joint"));
                }else if ((cmd[1].equals("set") || (cmd[1].equals("-s")))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.SetPlayerBalance(player.getName(), cmd[2], A, cmd[3]);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("reset")) || (cmd[1].equals("-rt"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.ResetPlayer(player.getName(), cmd[2], A);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("add")) || (cmd[1].equals("-a"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.AddPlayerBalance(player.getName(), cmd[2], A, cmd[3]);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("remove")) || (cmd.equals("-rm"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.RemovePlayerBalance(player.getName(), cmd[2], A, cmd[3]);
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("setauto")) || (cmd[1].equals("-sa"))){
                    if((player.canUseCommand("/dcauto")) || (player.canUseCommand("/dcadmin"))){
                        if (cmd.length > 2){
                            if (cmd[2].equalsIgnoreCase("OFF")){
                                return dCAH.PayForwardingSet(player.getName(), A);
                            }else{
                                return dCAH.PayForwardingSet(player.getName(), cmd[2]);
                            }
                        }
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if ((cmd[1].equals("auto")) || (cmd[1].equals("-au"))){
                    if((player.canUseCommand("/dcauto")) || (player.canUseCommand("/dcadmin"))){
                        return dCAH.CheckPayForwarding(player.getName());
                    }else{
                        return dCAH.Error(104, player.getName(), "");
                    }
                }else if(cmd[1].equals("ca")){
                    if(player.canUseCommand("/dcadmin")){
                        if(cmd.length > 2){
                            return dCAH.CreateAccount(player, cmd[2]);
                        }
                        else{
                            return dCAH.Error(114, player.getName(), "");
                        }
                    }
                }else{
                    return dCAH.DisplayBalance(player.getName(), 4, cmd[1], player.isAdmin());
                }
            }else{
                return dCAH.Error(104, player.getName(), "");
            }
        }else if (cmd[0].equals("/bank")){
            if ((player.canUseCommand("/bank")) || (player.canUseCommand("/dcadmin"))){
                if (cmd.length == 1){
                    return dCAH.DisplayBalance(player.getName(), 2, "", player.isAdmin());
                }else if (cmd[1].equals("withdraw") || (cmd[1].equals("-w"))){
                    if (cmd.length == 2){
                        return dCAH.Error(106, player.getName(), "");
                    }else{
                        return dCAH.BankWithdraw(player.getName(), cmd[2]);
                    }
                }else if (cmd[1].equals("deposit") || (cmd[1].equals("-d"))){
                    if (cmd.length == 2){
                        return dCAH.Error(106, player.getName(), "");
                    }else{
                        return dCAH.BankDeposit(player.getName(), cmd[2]);
                    }
                }else if ((cmd[1].equals("set")) || (cmd[1].equals("-s"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.SetPlayerBalance(player.getName(), cmd[2], B, cmd[3]);
                        }
                    }
                }else if ((cmd[1].equals("reset")) || (cmd[1].equals("-r"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else{
                            return dCAH.ResetPlayer(player.getName(), cmd[2], B);
                        }
                    }
                }else if ((cmd[1].equals("add")) || (cmd[1].equals("-a"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.AddPlayerBalance(player.getName(), cmd[2], B, cmd[3]);
                        }
                    }
                }else if ((cmd[1].equals("remove")) || (cmd[1].equals("-r"))){
                    if (player.canUseCommand("/dcadmin")){
                        if (cmd.length < 3){
                            return dCAH.Error(120, player.getName(), "");
                        }else if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.RemovePlayerBalance(player.getName(), cmd[2], B, cmd[3]);
                        }
                    }
                }else if ((cmd[1].equals("help")) || (cmd[1].equals("?"))){
                    if (cmd.length > 2){
                        if (cmd[2].equals("admin")){
                            if (player.canUseCommand("/dcadmin")){
                                return dCAH.BankAdminHelp(player.getName());
                            }
                        }
                    }
                    return dCAH.BankHelp(player.getName(), player.canUseCommand("/dcadmin"));
                }else{
                    return dCAH.DisplayBalance(player.getName(), 5, cmd[1], player.isAdmin());
                }
            }else{
                return dCAH.Error(104, player.getName(), "");
            }
        }else if (cmd[0].equals("/joint")){
            if ((player.canUseCommand("/joint")) || (player.canUseCommand("/dcadmin"))){
                if (cmd.length == 1){
                    dCAH.PlayerMessage(player.getName(), dCD.getHelpMessage(514));
                    return true;
                }else if (cmd.length == 2){
                    if ((cmd[1].equals("?")) || (cmd[1].equals("help"))){
                        return dCAH.JointHelp(player.getName(), player.canUseCommand("/dcadmin"), player.canUseCommand("/dccreate"));
                    }else{
                        return dCAH.DisplayBalance(player.getName(), 3, cmd[1], player.isAdmin());
                    }
                }else if (cmd.length > 2){
                    if ((cmd[1].equals("?")) || (cmd[1].equals("help"))){
                        if (cmd[2].equals("admin")){
                            if (player.canUseCommand("/dcadmin")){
                                return dCAH.JointAdminHelp(player.getName());
                            }
                        }
                    }
                    else if (cmd[2].equals("create") || cmd[2].equals("-c")){
                        if ((player.canUseCommand("/dccreate")) || (player.canUseCommand("/dcadmin"))){
                            return dCAH.CreateJointAccount(player.getName(), cmd[1]);
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else if (cmd[2].equals("delete") || cmd[2].equals("-del")){
                        if ((player.canUseCommand("/dccreate")) || (player.canUseCommand("/dcadmin"))){
                            return dCAH.DeleteJointAccount(player.getName(), cmd[1], player.canUseCommand("/dcadmin"));
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else if (cmd[2].equals("withdraw") || cmd[2].equals("-w")){
                        if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.JointWithdraw(player.getName(), cmd[1], cmd[3], player.canUseCommand("/dcadmin"));
                        }
                    }else if (cmd[2].equals("deposit") || cmd[2].equals("-d")){
                        if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.JointDeposit(player.getName(), cmd[1], cmd[3], player.canUseCommand("/dcadmin"));
                        }
                    }else if (cmd[2].equals("addowner") || cmd[2].equals("-ao")){
                        if (cmd.length < 4){
                            return dCAH.Error(112, player.getName(), "");
                        }else{
                            return dCAH.AddJointOwner(player.getName(), cmd[3], cmd[1], player.canUseCommand("/dcadmin"));
                        }
                    }else if (cmd[2].equals("removeowner") || cmd[2].equals("-ro")){
                        if (cmd.length < 4){
                            return dCAH.Error(112, player.getName(), "");
                        }else{
                            return dCAH.RemoveJointOwner(player.getName(), cmd[1], cmd[3], player.canUseCommand("/dcadmin"));
                        }
                    }else if ((cmd[2].equals("adduser")) || (cmd[2].equals("-au"))){
                        if (cmd.length < 4){
                            return dCAH.Error(121, player.getName(), "");
                        }else{
                            return dCAH.AddJointUser(player.getName(), cmd[3], cmd[1], player.canUseCommand("/dcadmin"));
                        }
                    }else if ((cmd[2].equals("removeuser")) || (cmd[2].equals("-ru"))){
                        if (cmd.length < 4){
                            return dCAH.Error(121, player.getName(), "");
                        }else{
                            return dCAH.RemoveJointUser(player.getName(), cmd[1], cmd[3], player.canUseCommand("/dcadmin"));
                        }
                    }else if (cmd[2].equals("pay") || cmd[2].equals("-p")){
                        if (cmd.length < 4){
                            return dCAH.Error(114, player.getName(), "");
                        }else if (cmd.length < 5){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.PayPlayerWithJoint(player.getName(), cmd[1], cmd[3], cmd[4], player.isAdmin());
                        }
                    }else if ((cmd[2].equals("setusermax")) || (cmd[2].equals("-su"))){
                        if (cmd.length < 4){
                            return dCAH.Error(106, player.getName(), "");
                        }else{
                            return dCAH.setJointUserMaxWithdraw(player.getName(), cmd[1], cmd[3], player.canUseCommand("/dcadmin"));
                        }
                    }else if ((cmd[2].equals("usermax")) || (cmd[2].equals("-um"))){
                        return dCAH.checkJointUserMaxWithdraw(player.getName(), cmd[1], player.canUseCommand("/dcadmin"));
                    }else if (cmd[2].equals("add") || cmd[2].equals("-a")){
                        if (player.canUseCommand("/dcadmin")){
                            if (cmd.length < 4){
                                return dCAH.Error(106, player.getName(), "");
                            }else{
                                return dCAH.addJointBalance(player.getName(), cmd[1], cmd[3]);
                            }
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else if ((cmd[1].equals("remove")) || (cmd[1].equals("-r"))){
                        if (player.canUseCommand("/dcadmin")){
                            if (cmd.length < 4){
                                return dCAH.Error(106, player.getName(), "");
                            }else{
                                return dCAH.removeJointBalance(player.getName(), cmd[1], cmd[3]);
                            }
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else if ((cmd[1].equals("reset")) || (cmd[1].equals("-r"))){
                        if (player.canUseCommand("/dcadmin")){
                            return dCAH.resetJointBalance(player.getName(), cmd[1]);
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else if ((cmd[1].equals("set")) || (cmd[1].equals("-s"))){
                        if (player.canUseCommand("/dcadmin")){
                            if (cmd.length < 4){
                                return dCAH.Error(106, player.getName(), "");
                            }else{
                                return dCAH.setJointBalance(player.getName(), cmd[1], cmd[3]);
                            }
                        }else{
                            return dCAH.Error(104, player.getName(), "");
                        }
                    }else{
                        return dCAH.Error(113, player.getName(), "");
                    }
                }
            }
        }
        else if(cmd[0].equalsIgnoreCase("/dConomy") || cmd[0].equalsIgnoreCase("/dCo")){
            player.sendMessage("§7-----§2dConomy by §aDarkDiplomat§7-----");
            player.sendMessage("§7-----§6"+dCo.codename+" Installed§7-----");
            if(!dCo.isLatest()){
                player.sendMessage("§7-----§6An update is availible! Latest = §2"+dCo.currver+"§7-----");
            }
            return true;
        }
        return false;
    }

    public boolean onConsoleCommand(String[] cmd){
        if (cmd[0].equals("money")){
            if (cmd.length < 2){ 
                dCD.log.info("Use /money help to display help!");
                return true;
            }else if ((cmd[1].equals("reset")) || (cmd[1].equals("-r"))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else{
                    return dCAH.ResetPlayer(S, cmd[2], A);
                }
            }else if ((cmd[1].equals("set") || (cmd[1].equals("-s")))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.SetPlayerBalance(S, cmd[2], A, cmd[3]);
                }
            }else if ((cmd[1].equals("add")) || (cmd[1].equals("-a"))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.AddPlayerBalance(S, cmd[2], A, cmd[3]);
                }
            }else if ((cmd[1].equals("remove")) || (cmd[1].equals("-r"))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.RemovePlayerBalance(S, cmd[2], A, cmd[3]);
                }
            }else if ((cmd[1].equals("help")) || (cmd[1].equals("?"))){
                return dCAH.ConsoleMoneyHelp();
            }
        }else if (cmd[0].equals("bank")){
            if (cmd.length < 2){ 
                dCD.log.info("ERROR");
            }else if (cmd[1].equals("reset")){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else{
                    return dCAH.ResetPlayer(S, cmd[2], B);
                }
            }else if (cmd[1].equals("set")){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.SetPlayerBalance(S, cmd[2], B, cmd[3]);
                }
            }else if ((cmd[1].equals("add")) || (cmd[1].equals("-a"))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.AddPlayerBalance(S, cmd[2], B, cmd[3]);
                }
            }else if ((cmd[1].equals("remove")) || (cmd[1].equals("-r"))){
                if (cmd.length < 3){
                    return dCAH.ConsoleError(120, "");
                }else if (cmd.length < 4){
                    return dCAH.ConsoleError(106, "");
                }else{
                    return dCAH.RemovePlayerBalance(S, cmd[2], A, cmd[3]);
                }
            }else if ((cmd[1].equals("help")) || (cmd[1].equals("?"))){
                return dCAH.ConsoleBankHelp();
            }
        }else if (cmd[0].equals("joint")){
            if (cmd.length > 1){
                if((cmd[1].equals("help")) || (cmd[1].equals("?"))){
                    return dCAH.ConsoleJointHelp();
                }
            }else if (cmd.length == 2){
                return dCAH.DisplayBalance(S, 3, cmd[1], true);
            }else if (cmd.length > 2){
                if (cmd[2].equals("create") || cmd[2].equals("-c")){
                    return dCAH.CreateJointAccount(S, cmd[1]);
                }else if (cmd[2].equals("delete") || cmd[2].equals("-del")){
                    return dCAH.DeleteJointAccount(S, cmd[1], true);
                }else if (cmd[2].equals("withdraw") || cmd[2].equals("-w")){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.JointWithdraw(S, cmd[1], cmd[3], true);
                    }
                }else if (cmd[2].equals("deposit") || cmd[2].equals("-d")){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.JointDeposit(S, cmd[1], cmd[3], true);
                    }
                }else if (cmd[2].equals("addowner") || cmd[2].equals("-ao")){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(112, "");
                    }else{
                        return dCAH.AddJointOwner(S, cmd[3], cmd[1], true);
                    }
                }else if (cmd[2].equals("removeowner") || cmd[2].equals("-ro")){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(112, "");
                    }else{
                        return dCAH.RemoveJointOwner(S, cmd[1], cmd[3], true);
                    }
                }else if ((cmd[2].equals("adduser")) || (cmd[2].equals("-au"))){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(121, "");
                    }else{
                        return dCAH.AddJointUser(S, cmd[3], cmd[1], true);
                    }
                }else if ((cmd[2].equals("removeuser")) || (cmd[2].equals("-ru"))){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(121, "");
                    }else{
                        return dCAH.RemoveJointUser(S, cmd[1], cmd[3], true);
                    }
                }else if ((cmd[2].equals("setusermax")) || (cmd[2].equals("-sum"))){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.setJointUserMaxWithdraw(S, cmd[1], cmd[2], true);
                    }
                }else if ((cmd[2].equals("usermax")) || (cmd[2].equals("-um"))){
                    return dCAH.checkJointUserMaxWithdraw(S, cmd[1], true);
                }else if (cmd[2].equals("add") || cmd[2].equals("-a")){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.addJointBalance(S, cmd[1], cmd[3]);
                    }
                }else if ((cmd[1].equals("remove")) || (cmd[1].equals("-r"))){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.removeJointBalance(S, cmd[1], cmd[3]);
                    }
                }else if ((cmd[1].equals("reset")) || (cmd[1].equals("-r"))){
                    return dCAH.resetJointBalance(S, cmd[1]);
                }else if ((cmd[1].equals("set")) || (cmd[1].equals("-s"))){
                    if (cmd.length < 4){
                        return dCAH.ConsoleError(106, "");
                    }else{
                        return dCAH.setJointBalance(S, cmd[1], cmd[3]);
                    }
                }
            }
        }
        return false;
    }
    
    public void onLogin(Player player){
        if(!player.canUseCommand("/money") && !dCD.CAA){
            return;
        }
        if (!dCD.keyExists(player.getName(), "Account")){
            dCD.setInitialBalance(dCD.getStartingBalance(), player.getName());
        }
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