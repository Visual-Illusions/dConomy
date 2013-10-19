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

import net.visualillusionsent.dconomy.api.MineChatForm;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.LocaleHelper;

import java.io.File;
import java.io.FileInputStream;

public final class MessageTranslator extends LocaleHelper {

    static {
        if (!new File(dCoBase.lang_dir).exists()) {
            new File(dCoBase.lang_dir).mkdirs();
        }
        else {
            try {
                if (!new File(dCoBase.lang_dir.concat("languages.txt")).exists()) {
                    moveLang("languages.txt");
                }
                else if (!FileUtils.md5SumMatch(MessageTranslator.class.getResourceAsStream("/resources/lang/languages.txt"), new FileInputStream(dCoBase.lang_dir.concat("languages.txt")))) {
                    moveLang("languages.txt");
                }
                if (!new File(dCoBase.lang_dir.concat("en_US.lang")).exists()) {
                    moveLang("en_US.lang");
                }
                else if (!FileUtils.md5SumMatch(MessageTranslator.class.getResourceAsStream("/resources/lang/en_US.lang"), new FileInputStream(dCoBase.lang_dir.concat("en_US.lang")))) {
                    moveLang("en_US.lang");
                }
            }
            catch (Exception ex) {
                throw new dConomyInitializationError("Failed to verify and move lang files", ex);
            }
        }
    }

    MessageTranslator() {
        super(true, dCoBase.lang_dir, dCoBase.getServerLocale());
        reloadLangFiles();
    }

    public final String translate(String key, String locale, Object... args) {
        String toRet = args != null ? colorForm(localeTranslate(key, locale, args)) : colorForm(localeTranslate(key, locale));
        if (toRet.contains("$m")) {
            toRet = toRet.replace("$m", dCoBase.getProperties().getString("money.name"));
        }
        return toRet;
    }

    private String colorForm(String msg) {
        return msg.replace("$c", MineChatForm.MARKER.stringValue());
    }

    private static void moveLang(String locale) {
        FileUtils.cloneFileFromJar(JarUtils.getJarPath(dCoBase.class), "resources/lang/languages.txt", dCoBase.lang_dir.concat("languages.txt"));
    }
}
