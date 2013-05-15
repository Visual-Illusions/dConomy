package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.utils.StringUtils;

public final class InformationCommand extends dConomyCommand{
    private final List<String> about;

    public InformationCommand(){
        super(0);
        List<String> pre = new ArrayList<String>();
        pre.add(center(MCChatForm.ORANGE + "-" + MCChatForm.LIGHT_GREEN + " dConomy Core v3.0 " + MCChatForm.ORANGE + "-"));

        pre.add(MCChatForm.ORANGE + "Lead Developer:" + MCChatForm.LIGHT_GREEN + " DarkDiplomat");
        // pre.add(MCChatForm.ORANGE + "Contributers:" + MCChatForm.LIGHT_GREEN+" "); //If someone adds to dConomy, their name can go here
        pre.add(MCChatForm.ORANGE + "Website:" + MCChatForm.LIGHT_GREEN + " http://wiki.visualillusionsent.net/dConomy");
        pre.add(MCChatForm.ORANGE + "Issues:" + MCChatForm.LIGHT_GREEN + " https://github.com/Visual-Illusions/dConomy/issues");

        // Next 3 lines should always remain at the end of the About
        pre.add(center("§aCopyright © 2011-2013 §2Visual §6I§9l§bl§4u§as§2i§5o§en§7s §2Entertainment"));
        about = Collections.unmodifiableList(pre);
    }

    public final void execute(Mod_User user, String[] args){
        for (String msg : about) {
            user.message(msg);
        }
    }

    private final String center(String toCenter){
        String strColorless = MCChatForm.removeFormating(toCenter);
        return StringUtils.padCharLeft(toCenter, (int) (Math.floor(63 - strColorless.length()) / 2), ' ');
    }
}
