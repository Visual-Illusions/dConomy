/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2013 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * dConomy is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with dConomy.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.visualillusionsent.dconomy.dCoBase;
import net.visualillusionsent.minecraft.server.mod.interfaces.MineChatForm;
import net.visualillusionsent.minecraft.server.mod.interfaces.ModUser;
import net.visualillusionsent.utils.StringUtils;
import net.visualillusionsent.utils.VersionChecker;

public final class InformationCommand extends dConomyCommand{
    private final List<String> about;

    public InformationCommand(){
        super(0);
        List<String> pre = new ArrayList<String>();
        pre.add(center(MineChatForm.CYAN + "---" + MineChatForm.LIGHT_GREEN + " d" + MineChatForm.ORANGE + "Conomy " + MineChatForm.PURPLE + "v" + dCoBase.getRawVersion() + MineChatForm.CYAN + " ---"));
        pre.add("$VERSION_CHECK$");
        pre.add(MineChatForm.CYAN + "Build: " + MineChatForm.LIGHT_GREEN + dCoBase.getBuildNumber());
        pre.add(MineChatForm.CYAN + "Built: " + MineChatForm.LIGHT_GREEN + dCoBase.getBuildTime());
        pre.add(MineChatForm.CYAN + "Lead Developer: " + MineChatForm.LIGHT_GREEN + "DarkDiplomat");
        pre.add(MineChatForm.CYAN + "Contributers: " + MineChatForm.LIGHT_GREEN + " "); // If someone adds to dConomy, their name can go here
        pre.add(MineChatForm.CYAN + "Website: " + MineChatForm.LIGHT_GREEN + "http://wiki.visualillusionsent.net/dConomy");
        pre.add(MineChatForm.CYAN + "Issues: " + MineChatForm.LIGHT_GREEN + "https://github.com/Visual-Illusions/dConomy/issues");

        // Next 2 lines should always remain at the end of the About
        pre.add(center("§aCopyright © 2011-2013 §2Visual §6I§9l§bl§4u§as§2i§5o§en§7s §2Entertainment"));
        about = Collections.unmodifiableList(pre);
    }

    public final void execute(ModUser user, String[] args){
        for (String msg : about) {
            if (msg.equals("$VERSION_CHECK$")) {
                VersionChecker vc = dCoBase.getVersionChecker();
                Boolean islatest = vc.isLatest();
                if (islatest == null) {
                    user.message(center(MineChatForm.GRAY + "VersionCheckerError: " + vc.getErrorMessage()));
                }
                else if (!vc.isLatest()) {
                    user.message(center(MineChatForm.GRAY + vc.getUpdateAvailibleMessage()));
                }
                else {
                    user.message(center(MineChatForm.LIGHT_GREEN + "Latest Version Installed"));
                }
            }
            else {
                user.message(msg);
            }
        }
    }

    private final String center(String toCenter){
        String strColorless = MineChatForm.removeFormating(toCenter);
        return StringUtils.padCharLeft(toCenter, (int) (Math.floor(63 - strColorless.length()) / 2), ' ');
    }
}
