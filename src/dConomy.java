import java.util.logging.Logger;

import net.visualillusionsent.dconomy.commands.CommandExecuter;
import net.visualillusionsent.dconomy.data.DCoProperties;

/**
 * dConomy.java - dConomy main plugin class
 * 
 * @author darkdiplomat
 * @version 2.0
 */
public class dConomy extends Plugin{
    private Logger logger = Logger.getLogger("Minecraft");
    private dCoListener dcl;
    private boolean isLoaded = false;

    /**
     * Run upon disabling dConomy
     */
    public void disable() {
        if(isLoaded){
            etc.getInstance().removeCommand("/money");
            etc.getInstance().removeCommand("/bank");
            etc.getInstance().removeCommand("/joint");
            etc.getInstance().removeCommand("/dConomy");
        }
        logger.info("dConomy disabled...");
    }

    /**
     * Runs upon enabling dConomy
     */
    public void enable() {
        if(!CommandExecuter.isLatest()){
            logger.info("[dConomy] - There is an update available! Current = v" + CommandExecuter.currver);
        }
        logger.info("dConomy v"+CommandExecuter.version+"Enabled!");
    }
    
    /**
     * Runs upon initialization of dConomy
     */
    public void initialize(){
        if( DCoProperties.load() ){
            dcl = new dCoListener();
            etc.getLoader().addListener(PluginLoader.Hook.COMMAND, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.LOGIN, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getInstance().addCommand("/dConomy", "- displays information about dConomy");
            etc.getInstance().addCommand("/money", "help (?) - display dConomy Money Help");
            etc.getInstance().addCommand("/bank", "help (?) - display dConomy Bank Help");
            etc.getInstance().addCommand("/joint","help (?) - display dConomy Joint Help");
            logger.info("dConomy Initialized!");
            isLoaded = true;
        }
        else{
            etc.getLoader().disablePlugin("dConomy");
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
