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
