import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class dConomy extends Plugin{
    public final String name = "dConomy";
    public final String codename = "Hina (1.5)";
    public String currver = "Hina (1.5)";
    public final Logger log = Logger.getLogger("Minecraft");
    protected dCData dCD;
    protected dCListener dCL;
    protected dCHook dCH;
    protected dCTimer dCT;
    
    static boolean Terminated = false;
    
    public void enable(){
        if(!isLatest()){
            log.info("[dConomy] - There is an update available! Current = " + currver);
        }
        log.info(name + " CodeName: "+codename+" Enabled!");
    }

    public void disable(){
        dCT.cancel();
        etc.getInstance().removeCommand("/money");
        etc.getInstance().removeCommand("/bank");
        etc.getInstance().removeCommand("/joint");
        etc.getLoader().removeCustomListener(dCH.dCBalance.getName());
        log.info(name + " CodeName: "+codename+" Disabled!");
    }

    public void initialize(){
        dCD = new dCData();
        dCL = new dCListener(this, dCD);
        dCH = new dCHook(this);
        dCT = dCD.dCT;
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, dCL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.LOGIN, dCL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, dCL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addCustomListener(dCH.dCBalance);
        etc.getInstance().addCommand("/money", "help (?) - display dConomy Money Help");
        etc.getInstance().addCommand("/bank", "help (?) - display dConomy Bank Help");
        etc.getInstance().addCommand("/joint","help (?) - display dConomy Joint Help");
        log.info(name +" CodeName: "+codename+" Initialized!");
    }
    
    /*Check if running the latest version of dConomy*/
    public boolean isLatest(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name).openStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                currver = inputLine;
            }
            in.close();
            return codename.equals(currver);
        } 
        catch (Exception E) {
        }
        return true;
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