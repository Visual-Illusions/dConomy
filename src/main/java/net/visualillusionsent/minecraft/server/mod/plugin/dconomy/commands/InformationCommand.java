/* 
 * Copyright 2011 - 2013 Visual Illusions Entertainment.
 *  
 * This file is part of dConomy.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html
 * 
 * Source Code available @ https://github.com/Visual-Illusions/dConomy
 */
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.visualillusionsent.minecraft.server.mod.interfaces.MCChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_User;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.dCoBase;
import net.visualillusionsent.utils.StringUtils;
import net.visualillusionsent.utils.VersionChecker;

public final class InformationCommand extends dConomyCommand{
    private final List<String> about;

    public InformationCommand(){
        super(0);
        List<String> pre = new ArrayList<String>();
        pre.add(center(MCChatForm.CYAN + "---" + MCChatForm.LIGHT_GREEN + " d" + MCChatForm.ORANGE + "Conomy " + MCChatForm.PURPLE + "v" + dCoBase.getRawVersion() + MCChatForm.CYAN + " ---"));
        pre.add("$VERSION_CHECK$");
        pre.add(MCChatForm.CYAN + "Lead Developer:" + MCChatForm.LIGHT_GREEN + " DarkDiplomat");
        pre.add(MCChatForm.CYAN + "Contributers:" + MCChatForm.LIGHT_GREEN + " "); // If someone adds to dConomy, their name can go here
        pre.add(MCChatForm.CYAN + "Website:" + MCChatForm.LIGHT_GREEN + " http://wiki.visualillusionsent.net/dConomy");
        pre.add(MCChatForm.CYAN + "Issues:" + MCChatForm.LIGHT_GREEN + " https://github.com/Visual-Illusions/dConomy/issues");

        // Next 3 lines should always remain at the end of the About
        pre.add(center("§aCopyright © 2011-2013 §2Visual §6I§9l§bl§4u§as§2i§5o§en§7s §2Entertainment"));
        about = Collections.unmodifiableList(pre);
    }

    public final void execute(Mod_User user, String[] args){
        for (String msg : about) {
            if (msg.equals("$VERSION_CHECK$")) {
                VersionChecker vc = dCoBase.getVersionChecker();
                Boolean islatest = vc.isLatest();
                if (islatest == null) {
                    user.message(center(MCChatForm.GRAY + "VersionCheckerError: " + vc.getErrorMessage()));
                }
                else if (!vc.isLatest()) {
                    user.message(center(MCChatForm.GRAY + vc.getUpdateAvailibleMessage()));
                }
                else {
                    user.message(center(MCChatForm.LIGHT_GREEN + "Latest Version Installed"));
                }
            }
            else {
                user.message(msg);
            }
        }
    }

    private final String center(String toCenter){
        String strColorless = MCChatForm.removeFormating(toCenter);
        return StringUtils.padCharLeft(toCenter, (int) (Math.floor(63 - strColorless.length()) / 2), ' ');
    }
}
