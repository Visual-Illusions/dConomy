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
package net.visualillusionsent.dconomy;

import net.visualillusionsent.minecraft.server.mod.interfaces.MineChatForm;
import net.visualillusionsent.utils.LocaleHelper;

public final class MessageTranslator extends LocaleHelper {

    private final static MessageTranslator $;
    static {
        $ = new MessageTranslator();
    }

    private MessageTranslator() {
        super(true, "config/dConomy3/lang/", null);
        reloadLangFiles();
    }

    public static final String translate(String key, String locale) {
        return colorForm($.localeTranslate(key, locale));
    }

    public static final String translate(String key, String locale, Object... args) {
        String toRet = colorForm($.localeTranslate(key, locale, args));
        if (toRet.contains("$m")) {
            toRet = toRet.replace("$m", dCoBase.getProperties().getString("money.name"));
        }
        return toRet;
    }

    private static final String colorForm(String msg) {
        return msg.replace("$c", MineChatForm.MARKER.stringValue());
    }

    public static final void reloadMessages() {
        $.reloadLangFiles();
    }
}
