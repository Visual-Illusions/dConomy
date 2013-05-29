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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.MineChatForm;
import net.visualillusionsent.utils.LocaleHelper;

public final class MessageTranslator extends LocaleHelper{

    private final static MessageTranslator $;
    static {
        $ = new MessageTranslator();
    }

    private MessageTranslator(){}

    public static final String transMessage(String key){
        return colorForm($.localeTranslate(key));
    }

    public static final String transFormMessage(String key, boolean color, Object... args){
        String toRet = $.localeTranslateMessage(key, args);
        if (color) {
            toRet = colorForm(toRet);
        }

        if (toRet.contains("$m")) {
            toRet = toRet.replace("$m", dCoBase.getProperties().getString("money.name"));
        }
        return toRet;
    }

    private static final String colorForm(String msg){
        return msg.replace("$c", MineChatForm.MARKER.stringValue());
    }
}
