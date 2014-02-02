/*
 * This file is part of dConomy.
 *
 * Copyright © 2011-2014 Visual Illusions Entertainment
 *
 * dConomy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see http://www.gnu.org/licenses/gpl.html.
 */
package net.visualillusionsent.dconomy;

import net.visualillusionsent.dconomy.api.dConomyUser;
import net.visualillusionsent.minecraft.plugin.CommandTabCompleteUtil;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason (darkdiplomat)
 */
public class TabCompleter extends CommandTabCompleteUtil {
    private static final String[] walletSubsA = new String[]{ "pay", "add", "remove", "set", "reload", "reset", "lock" };
    private static final Matcher matchA = Pattern.compile("(reload|reset)").matcher(""),
            matchB = Pattern.compile("(pay|add|remove|set|lock)").matcher("");

    public static List<String> match(dConomyUser user, String[] args) {
        if (args.length == 1) {
            List<String> preRet = matchTo(args, walletSubsA);
            Iterator<String> preRetItr = preRet.iterator();
            while (preRetItr.hasNext()) {
                String ret = preRetItr.next();
                if (ret.equals("pay") && !user.hasPermission("dconomy.wallet.pay")) {
                    preRetItr.remove();
                }
                else if (!user.hasPermission("dconomy.admin.wallet.".concat(ret))) {
                    preRetItr.remove();
                }
            }
            return preRet;
        }
        else if ((args.length == 2 && matchA.reset(args[0]).matches())
                || (args.length == 3 && matchB.reset(args[0]).matches())) {
            return matchTo(args, dCoBase.getServer().getUserNames());
        }
        return null;
    }
}
