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

import net.visualillusionsent.minecraft.plugin.ChatFormat;
import net.visualillusionsent.minecraft.plugin.PluginInitializationException;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.JarUtils;
import net.visualillusionsent.utils.LocaleHelper;

import java.io.File;
import java.io.FileInputStream;

public final class MessageTranslator extends LocaleHelper {
    private static final String lang_dir = "lang/dConomy3/";

    static {
        if (!new File(lang_dir).exists()) {
            new File(lang_dir).mkdirs();
        }
        try {
            if (!new File(lang_dir.concat("languages.txt")).exists()) {
                moveLang("languages.txt");
            }
            else if (!FileUtils.md5SumMatch(dConomy.class.getResourceAsStream("/resources/lang/languages.txt"), new FileInputStream(lang_dir.concat("languages.txt")))) {
                moveLang("languages.txt");
            }
            if (!new File(lang_dir.concat("en_US.lang")).exists()) {
                moveLang("en_US.lang");
            }
            else if (!FileUtils.md5SumMatch(dConomy.class.getResourceAsStream("/resources/lang/en_US.lang"), new FileInputStream(lang_dir.concat("en_US.lang")))) {
                moveLang("en_US.lang");
            }
        }
        catch (Exception ex) {
            throw new PluginInitializationException("Failed to verify and move lang files", ex);
        }
    }

    MessageTranslator() {
        super(true, lang_dir, dCoBase.getServerLocale());
    }

    public final String translate(String key, String locale, Object... args) {
        return ChatFormat.formatString(localeTranslate(key, locale, args), "$c").replace("$m", dCoBase.getMoneyName());
    }

    private static void moveLang(String locale) {
        FileUtils.cloneFileFromJar(JarUtils.getJarPath(dConomy.class), "resources/lang/".concat(locale), lang_dir.concat(locale));
    }
}
