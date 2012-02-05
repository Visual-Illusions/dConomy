import java.util.logging.Logger;

/**
* dConomy v1.x
* Copyright (C) 2011-2012 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@hotmail.com>
*
* This file is part of dConomy.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.
*/

public class dConomy extends Plugin{
	String name = "[dConomy]";
	String codename = "Mizuho";
	//version = 1.4
	Logger log = Logger.getLogger("Minecraft");
	static dCData dCD;
	static dCListener dCL;
	static dCHook dCH;
	dCTimer.dCBankTimer dCBT;
	dCTimer.dCJointWithdrawDelayTimer dCJWDT;
	PropertiesFile props;
	
	static boolean Terminated = false;
	
	public void enable(){
		log.info(name + " CodeName: "+codename+" Enabled!");
	}

	public void disable(){
		dCBT.cancel();
		dCJWDT.cancel();
		etc.getInstance().removeCommand("/money");
		etc.getInstance().removeCommand("/bank");
		etc.getInstance().removeCommand("/joint");
		log.info(name + " CodeName: "+codename+" Disabled!");
	}

	public void initialize(){
		props = getPropertiesFile();
		dCD = new dCData();
		dCL = new dCListener();
		dCH = new dCHook();
		dCBT = dCD.dCBT;
		dCJWDT = dCD.dCJWDT;
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, dCL, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.LOGIN, dCL, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, dCL, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addCustomListener(dCH.dCBalance);
		etc.getInstance().addCommand("/money", "help (?) - display dConomy Money Help");
		etc.getInstance().addCommand("/bank", "help (?) - display dConomy Bank Help");
		etc.getInstance().addCommand("/joint","help (?) - display dConomy Joint Help");
		log.info(name +" CodeName: "+codename+" Initialized!");
	}
}