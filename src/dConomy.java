import java.util.logging.Logger;

import net.visualillusionsent.dconomy.commands.CommandExecuter;
import net.visualillusionsent.dconomy.data.DCoProperties;

/**
 * dConomy main plugin class
 * <p>
 * Copyright (C) 2011-2012 Visual Illusions Entertainment
 * <p>
 * <a href="http://visualillusionsent.net/">http://visualillusionsent.net/</a>
 * <p>
 * dConomy is a plugin for the CanaryMod Minecraft Server Modification.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <a href="http://www.gnu.org/licenses/gpl.html">http://www.gnu.org/licenses/gpl.html</a>.
 * 
 * @author darkdiplomat
 * 
 * @version 2.0
 */
public class dConomy extends Plugin{
    private Logger logger = Logger.getLogger("Minecraft");
    private dCoListener dcl;
    private dCBalance dcb;
    private boolean isLoaded = false;

    /**
     * Runs upon disabling dConomy
     */
    public void disable() {
        if(isLoaded){
            etc.getInstance().removeCommand("/money");
            etc.getInstance().removeCommand("/bank");
            etc.getInstance().removeCommand("/joint");
            etc.getInstance().removeCommand("/dConomy");
            DCoProperties.getDS().terminateThreads();
            DCoProperties.getDS().saveMaps();
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
        logger.info("dConomy v"+CommandExecuter.version+" RC2 Enabled!");
    }
    
    /**
     * Runs upon initialization of dConomy
     * 
     * @see DCoProperties#load()
     */
    public void initialize(){
        if( DCoProperties.load() ){
            dcl = new dCoListener();
            dcb = new dCBalance();
            etc.getLoader().addListener(PluginLoader.Hook.COMMAND, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.LOGIN, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, dcl, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addCustomListener(dcb);
            etc.getInstance().addCommand("/dConomy", "- displays information about dConomy");
            etc.getInstance().addCommand("/money", "help - display dConomy Money Help");
            etc.getInstance().addCommand("/bank", "help - display dConomy Bank Help");
            etc.getInstance().addCommand("/joint","help - display dConomy Joint Help");
            logger.info("dConomy Initialized!");
            isLoaded = true;
        }
        else{
            etc.getLoader().disablePlugin("dConomy");
        }
    }
}
