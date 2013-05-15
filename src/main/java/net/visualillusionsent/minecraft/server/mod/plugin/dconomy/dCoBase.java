package net.visualillusionsent.minecraft.server.mod.plugin.dconomy;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.visualillusionsent.lang.InitializationError;
import net.visualillusionsent.minecraft.server.mod.interfaces.Mod_Server;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.accounting.wallet.WalletHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.DataSourceType;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.dCoDataHandler;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.dCoProperties;
import net.visualillusionsent.minecraft.server.mod.plugin.dconomy.io.logging.dCoLevel;

public final class dCoBase{

    private final dCoDataHandler handler;
    private final dCoProperties props;
    private final Logger logger;
    private static dCoBase $;
    private static Mod_Server server;

    public dCoBase(Mod_Server serv, Logger logger){
        try {
            $ = this;
            this.logger = logger;
            server = serv;
            props = new dCoProperties();
            handler = new dCoDataHandler(DataSourceType.XML);
        }
        catch (Exception ex) {
            throw new InitializationError(ex);
        }
    }

    public final static dCoDataHandler getDataHandler(){
        return $.handler;
    }

    public final static dCoProperties getProperties(){
        return $.props;
    }

    public final static Mod_Server getServer(){
        return server;
    }

    public static void info(String msg){
        $.logger.info(msg);
    }

    public static void info(String msg, Throwable thrown){
        $.logger.log(Level.INFO, msg, thrown);
    }

    public static void warning(String msg){
        $.logger.warning(msg);
    }

    public static void warning(String msg, Throwable thrown){
        $.logger.log(Level.WARNING, msg, thrown);
    }

    public static void severe(String msg){
        $.logger.severe(msg);
    }

    public static void severe(String msg, Throwable thrown){
        $.logger.log(Level.SEVERE, msg, thrown);
    }

    public static void stacktrace(Throwable thrown){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.STACKTRACE, "Stacktrace: ", thrown);
        }
    }

    public static void debug(String msg){
        if (dCoBase.getProperties().getBooleanValue("debug.enabled")) {
            $.logger.log(dCoLevel.GENERAL, msg);
        }
    }

    public final void cleanUp(){
        WalletHandler.cleanUp();
    }
}
