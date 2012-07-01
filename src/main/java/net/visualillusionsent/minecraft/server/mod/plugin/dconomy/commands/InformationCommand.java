package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands;

import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Caller;

public final class InformationCommand extends dConomyCommand{

    public InformationCommand(){
        super(0);
    }

    public final void execute(Mod_Caller caller, String[] args){
        caller.sendMessage(MCChatForm.ORANGE + "---" + MCChatForm.LIGHT_GREEN + " dConomy Core v3.0 " + MCChatForm.ORANGE + "---");
        caller.sendMessage(MCChatForm.ORANGE + "Developer: " + MCChatForm.LIGHT_GREEN + "DarkDiplomat");
        caller.sendMessage(MCChatForm.ORANGE + "Website: " + MCChatForm.LIGHT_GREEN + "http://wiki.visualillusionsent.net/dConomy");
        caller.sendMessage(MCChatForm.ORANGE + "Issues: ");
    }
}