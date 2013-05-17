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
package net.visualillusionsent.minecraft.server.mod.plugin.dconomy.data;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import net.visualillusionsent.utils.FileUtils;
import net.visualillusionsent.utils.PropertiesFile;

public final class dCoProperties{

    private final PropertiesFile propsFile;
    private final String configDir;

    public dCoProperties(){
        configDir = "config/dConomy3/";

        File dir = new File(configDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File real = new File(configDir.concat("settings.cfg"));
        if (!real.exists()) {
            FileUtils.cloneFileFromJar(getJarPath(), "resources/default_config.cfg", configDir.concat("settings.cfg"));
        }
        propsFile = new PropertiesFile(configDir.concat("settings.cfg"));
        // TODO Test Properties
    }

    public final void reloadProperties(){
        propsFile.reload();
    }

    public final String getString(String key){
        return propsFile.getString(key);
    }

    public final double getDouble(String key){
        return propsFile.getDouble(key);
    }

    public final boolean getBooleanValue(String key){
        return propsFile.getBoolean(key);
    }

    public final void setServerBalance(double value){
        propsFile.setDouble("server.balance", value);
        propsFile.save();
    }

    private final String getJarPath(){ // For when the jar isn't dConomy3.jar
        try {
            CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();
            return codeSource.getLocation().toURI().getPath();
        }
        catch (URISyntaxException ex) {}
        return "plugins/dConomy3.jar";
    }

    public final String getConfigurationDirectory(){
        return configDir;
    }
}
